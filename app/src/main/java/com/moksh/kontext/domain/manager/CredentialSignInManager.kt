package com.moksh.kontext.domain.manager

import android.content.Context
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.moksh.kontext.BuildConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CredentialSignInManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val credentialManager = CredentialManager.create(context)

    private fun createGoogleIdOption(nonce: String? = null): GetGoogleIdOption {
        Log.d(
            "CredentialSignInManager",
            "Creating Google ID option with client ID: ${BuildConfig.GCP_CLIENT_ID}"
        )
        return GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false) // Allow account selection
            .setServerClientId(BuildConfig.GCP_CLIENT_ID)
            .setAutoSelectEnabled(true)
            .apply {
                nonce?.let { setNonce(it) }
            }
            .build()
    }

    suspend fun signInWithGoogle(nonce: String? = null): Result<String> {
        return try {
            Log.d("CredentialSignInManager", "Starting Google Sign-In with Credential Manager")

            val googleIdOption = createGoogleIdOption(nonce)
            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result = credentialManager.getCredential(
                request = request,
                context = context
            )

            handleSignInResult(result)
        } catch (e: GetCredentialException) {
            Log.e("CredentialSignInManager", "Sign-in failed", e)
            Result.failure(e)
        }
    }

    private fun handleSignInResult(result: GetCredentialResponse): Result<String> {
        val credential = result.credential

        return when {
            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL -> {
                try {
                    val googleIdTokenCredential =
                        GoogleIdTokenCredential.createFrom(credential.data)
                    val idToken = googleIdTokenCredential.idToken

                    Log.d(
                        "CredentialSignInManager",
                        "Sign-in successful. ID Token obtained: ${idToken.take(20)}..."
                    )
                    Log.d(
                        "CredentialSignInManager",
                        "User: ${googleIdTokenCredential.displayName} (${googleIdTokenCredential.id})"
                    )

                    Result.success(idToken)
                } catch (e: Exception) {
                    Log.e("CredentialSignInManager", "Failed to extract ID token", e)
                    Result.failure(e)
                }
            }

            else -> {
                Log.e("CredentialSignInManager", "Unexpected credential type: ${credential.type}")
                Result.failure(Exception("Unexpected credential type"))
            }
        }
    }

    suspend fun signOut() {
        try {
            Log.d("CredentialSignInManager", "Signing out and clearing credential state")
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
        } catch (e: Exception) {
            Log.e("CredentialSignInManager", "Failed to clear credential state", e)
        }
    }
}