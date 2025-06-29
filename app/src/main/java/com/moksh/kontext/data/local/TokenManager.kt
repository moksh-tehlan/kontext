package com.moksh.kontext.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        PREFS_NAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveTokens(accessToken: String, refreshToken: String) {
        sharedPreferences.edit {
            putString(ACCESS_TOKEN_KEY, accessToken)
                .putString(REFRESH_TOKEN_KEY, refreshToken)
        }
    }

    fun getAccessToken(): String? {
        return sharedPreferences.getString(ACCESS_TOKEN_KEY, null)
    }

    fun getRefreshToken(): String? {
        return sharedPreferences.getString(REFRESH_TOKEN_KEY, null)
    }

    fun clearTokens() {
        sharedPreferences.edit {
            remove(ACCESS_TOKEN_KEY)
                .remove(REFRESH_TOKEN_KEY)
        }
    }

    fun hasValidTokens(): Boolean {
        return !getAccessToken().isNullOrEmpty() && !getRefreshToken().isNullOrEmpty()
    }

    companion object {
        private const val PREFS_NAME = "kontext_secure_prefs"
        private const val ACCESS_TOKEN_KEY = "access_token"
        private const val REFRESH_TOKEN_KEY = "refresh_token"
    }
}