package com.moksh.kontext.presentation.screens.otp.viewmodel

// Actions are the action performed by the user like button press, otp digit entry
sealed interface OtpScreenActions {
    data class OnOtpChanged(val otp: String) : OtpScreenActions
    data object OnVerifyOtp : OtpScreenActions
    data object OnResendOtp : OtpScreenActions
    data object OnBackPressed : OtpScreenActions
    data object OnChangeEmail : OtpScreenActions
}
