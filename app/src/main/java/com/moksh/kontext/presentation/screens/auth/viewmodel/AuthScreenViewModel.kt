package com.moksh.kontext.presentation.screens.auth.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import com.moksh.kontext.R
import javax.inject.Inject

@HiltViewModel
class AuthScreenViewModel @Inject constructor(
    @ApplicationContext private val context: Context
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
                // Handle terms click - open URL or show terms screen
            }
            is AuthScreenActions.OnUsagePolicyClick -> {
                // Handle usage policy click - open URL or show policy screen
            }
            is AuthScreenActions.OnPrivacyPolicyClick -> {
                // Handle privacy policy click - open URL or show policy screen
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
        
        // Simulate auth process
        viewModelScope.launch {
            try {
                // TODO: Implement actual email authentication
                // For now, just simulate success
                kotlinx.coroutines.delay(1000)
                _authEvents.emit(AuthScreenEvents.NavigateToHome)
            } catch (e: Exception) {
                _authState.value = _authState.value.copy(
                    emailError = context.getString(R.string.auth_failed_error)
                )
            } finally {
                _authState.value = _authState.value.copy(isEmailLoading = false)
            }
        }
    }

    private fun handleGoogleSignIn() {
        _authState.value = _authState.value.copy(isGoogleLoading = true, emailError = null)
        
        // Simulate Google auth process
        viewModelScope.launch {
            try {
                // TODO: Implement actual Google authentication
                // For now, just simulate success
                kotlinx.coroutines.delay(1000)
                _authEvents.emit(AuthScreenEvents.NavigateToHome)
            } catch (e: Exception) {
                _authState.value = _authState.value.copy(
                    emailError = context.getString(R.string.google_signin_error)
                )
            } finally {
                _authState.value = _authState.value.copy(isGoogleLoading = false)
            }
        }
    }
}