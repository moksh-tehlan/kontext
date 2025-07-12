package com.moksh.kontext.presentation.screens.auth.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moksh.kontext.BuildConfig
import com.moksh.kontext.R
import com.moksh.kontext.domain.manager.CredentialSignInManager
import com.moksh.kontext.domain.repository.AuthRepository
import com.moksh.kontext.domain.utils.Result
import com.moksh.kontext.presentation.core.utils.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthScreenViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authRepository: AuthRepository,
    private val credentialSignInManager: CredentialSignInManager
) : ViewModel() {
    private val _authState = MutableStateFlow(AuthScreenState())
    val authState = _authState.asStateFlow()
        .onStart { }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            AuthScreenState()
        )

    private val _authEvents = MutableSharedFlow<AuthScreenEvents>()
    val authEvents = _authEvents.asSharedFlow()

    fun onAction(action: AuthScreenActions) {
        when (action) {
            is AuthScreenActions.OnEmailChanged -> {
                _authState.value = _authState.value.copy(
                    email = action.email,
                    emailError = null
                )
            }
            is AuthScreenActions.OnContinueWithEmail -> {
                handleEmailSignIn()
            }
            is AuthScreenActions.OnContinueWithGoogle -> {
                handleGoogleSignIn()
            }
            is AuthScreenActions.OnTermsClick -> {
                viewModelScope.launch {
                    _authEvents.emit(AuthScreenEvents.OpenExternalLink(BuildConfig.TERMS_URL))
                }
            }
            is AuthScreenActions.OnPrivacyPolicyClick -> {
                viewModelScope.launch {
                    _authEvents.emit(AuthScreenEvents.OpenExternalLink(BuildConfig.PRIVACY_URL))
                }
            }
        }
    }

    private fun handleEmailSignIn() {
        val email = _authState.value.email.trim()
        
        // Basic email validation
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _authState.value = _authState.value.copy(
                emailError = context.getString(R.string.auth_email_validation_error)
            )
            return
        }

        _authState.value = _authState.value.copy(isEmailLoading = true, emailError = null)
        
        viewModelScope.launch {
            when (val result = authRepository.sendOtp(email)) {
                is Result.Success -> {
                    _authEvents.emit(AuthScreenEvents.NavigateToOtp(email))
                }

                is Result.Error -> {
                    _authState.value = _authState.value.copy(
                        emailError = result.error.asUiText().asString(context)
                    )
                }
            }
            _authState.value = _authState.value.copy(isEmailLoading = false)
        }
    }

    private fun handleGoogleSignIn() {
        Log.d("AuthScreenViewModel", "handleGoogleSignIn called")
        _authState.value = _authState.value.copy(isGoogleLoading = true, emailError = null)
        
        viewModelScope.launch {
            try {
                Log.d("AuthScreenViewModel", "Starting Credential Manager sign-in")
                val result = credentialSignInManager.signInWithGoogle()
          
                if (result.isSuccess) {
                    val idToken = result.getOrThrow()
                    Log.d("AuthScreenViewModel", "Got ID token, calling backend")

                    when (val authResult = authRepository.googleLogin(idToken)) {
                        is Result.Success -> {
                            Log.d(
                                "AuthScreenViewModel",
                                "Google login successful, navigating to home"
                            )
                            _authEvents.emit(AuthScreenEvents.NavigateToHome)
                        }

                        is Result.Error -> {
                            Log.e(
                                "AuthScreenViewModel",
                                "Backend login failed: ${authResult.error}"
                            )
                            _authState.value = _authState.value.copy(
                                emailError = authResult.error.asUiText().asString(context)
                            )
                        }
                    }
                } else {
                    Log.e(
                        "AuthScreenViewModel",
                        "Credential Manager sign-in failed",
                        result.exceptionOrNull()
                    )
                    _authState.value = _authState.value.copy(
                        emailError = context.getString(R.string.google_signin_error)
                    )
                }
            } catch (e: Exception) {
                Log.e("AuthScreenViewModel", "Exception during Google sign-in", e)
                _authState.value = _authState.value.copy(
                    emailError = context.getString(R.string.google_signin_error)
                )
            } finally {
                _authState.value = _authState.value.copy(isGoogleLoading = false)
            }
        }
    }
}