package com.moksh.kontext.presentation.screens.otp.viewmodel

import com.moksh.kontext.presentation.core.utils.UiText

data class OtpScreenState(
    val email: String = "",
    val otp: String = "",
    val isVerifying: Boolean = false,
    val isResending: Boolean = false,
    val otpError: UiText? = null,
    val successMessage: String? = null,
    val resendCooldown: Int = 0, // Cooldown in seconds before resend is available
    val showResendButton: Boolean = false
)
