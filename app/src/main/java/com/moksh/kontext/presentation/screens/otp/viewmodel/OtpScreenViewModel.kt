package com.moksh.kontext.presentation.screens.otp.viewmodel

import android.content.Context
import androidx.lifecycle.SavedStateHandle
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
import kotlinx.coroutines.delay
import com.moksh.kontext.R
import com.moksh.kontext.presentation.navigation.AuthRoutes
import javax.inject.Inject

@HiltViewModel
class OtpScreenViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    
    private val _otpState = MutableStateFlow(OtpScreenState())
    val otpState = _otpState.asStateFlow()
        .onStart {
            val email = savedStateHandle.get<String>("email")
            if (email != null) {
                initWithEmail(email)
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            OtpScreenState()
        )

    private val _otpEvents = MutableSharedFlow<OtpScreenEvents>()
    val otpEvents = _otpEvents.asSharedFlow()

    fun initWithEmail(email: String) {
        _otpState.value = _otpState.value.copy(
            email = email,
            showResendButton = false,
            resendCooldown = 60 // Start with 60 seconds cooldown
        )
        startResendCooldown()
    }

    fun onAction(action: OtpScreenActions) {
        when (action) {
            is OtpScreenActions.OnOtpChanged -> {
                // Only allow digits and limit to 6 characters
                val cleanOtp = action.otp.filter { it.isDigit() }.take(6)
                _otpState.value = _otpState.value.copy(
                    otp = cleanOtp,
                    otpError = null,
                    successMessage = null
                )
                if(cleanOtp.length == 6 ){
                    handleOtpVerification()
                }
            }
            is OtpScreenActions.OnVerifyOtp -> {
                handleOtpVerification()
            }
            is OtpScreenActions.OnResendOtp -> {
                handleResendOtp()
            }
            is OtpScreenActions.OnBackPressed -> {
                viewModelScope.launch {
                    _otpEvents.emit(OtpScreenEvents.NavigateBack)
                }
            }
            is OtpScreenActions.OnChangeEmail -> {
                viewModelScope.launch {
                    _otpEvents.emit(OtpScreenEvents.NavigateBack)
                }
            }
        }
    }

    private fun handleOtpVerification() {
        val otp = _otpState.value.otp
        
        // Basic OTP validation
        if (otp.length != 6) {
            _otpState.value = _otpState.value.copy(
                otpError = context.getString(R.string.otp_validation_error)
            )
            return
        }

        _otpState.value = _otpState.value.copy(isVerifying = true, otpError = null)
        
        // Simulate OTP verification process
        viewModelScope.launch {
            try {
                // TODO: Implement actual OTP verification
                // For now, just simulate success with any 6-digit code
                delay(1500)
                _otpEvents.emit(OtpScreenEvents.NavigateToHome)
            } catch (e: Exception) {
                _otpState.value = _otpState.value.copy(
                    otpError = context.getString(R.string.otp_verification_failed)
                )
            } finally {
                _otpState.value = _otpState.value.copy(isVerifying = false)
            }
        }
    }

    private fun handleResendOtp() {
        if (_otpState.value.isResending || !_otpState.value.showResendButton) {
            return
        }

        _otpState.value = _otpState.value.copy(
            isResending = true,
            otpError = null
        )
        
        // Simulate resend OTP process
        viewModelScope.launch {
            try {
                // TODO: Implement actual OTP resend
                delay(1000)
                _otpState.value = _otpState.value.copy(
                    successMessage = context.getString(R.string.otp_resent_success)
                )
                
                // Reset cooldown
                _otpState.value = _otpState.value.copy(
                    showResendButton = false,
                    resendCooldown = 60
                )
                startResendCooldown()
                
            } catch (e: Exception) {
                _otpState.value = _otpState.value.copy(
                    otpError = context.getString(R.string.otp_resend_failed)
                )
            } finally {
                _otpState.value = _otpState.value.copy(isResending = false)
            }
        }
    }

    private fun startResendCooldown() {
        viewModelScope.launch {
            while (_otpState.value.resendCooldown > 0) {
                delay(1000)
                val currentCooldown = _otpState.value.resendCooldown
                if (currentCooldown > 0) {
                    _otpState.value = _otpState.value.copy(
                        resendCooldown = currentCooldown - 1
                    )
                }
            }
            _otpState.value = _otpState.value.copy(showResendButton = true)
        }
    }
}
