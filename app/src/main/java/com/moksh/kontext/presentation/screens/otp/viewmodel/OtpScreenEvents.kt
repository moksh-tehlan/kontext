package com.moksh.kontext.presentation.screens.otp.viewmodel

// Events are one time event get triggered by viewmodel like move to next screen and something like that
sealed interface OtpScreenEvents {
    data class ShowError(val message: String) : OtpScreenEvents
    data object NavigateToHome : OtpScreenEvents
    data object NavigateBack : OtpScreenEvents
}
