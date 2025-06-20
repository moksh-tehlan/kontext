package com.moksh.kontext.presentation.screens.auth.viewmodel

// Actions are the action performed by the user like button press, email fillout
sealed interface AuthScreenActions {
    data class OnEmailChanged(val email: String) : AuthScreenActions
    data object OnContinueWithEmail : AuthScreenActions
    data object OnContinueWithGoogle : AuthScreenActions
    data object OnTermsClick : AuthScreenActions
    data object OnUsagePolicyClick : AuthScreenActions
    data object OnPrivacyPolicyClick : AuthScreenActions
}