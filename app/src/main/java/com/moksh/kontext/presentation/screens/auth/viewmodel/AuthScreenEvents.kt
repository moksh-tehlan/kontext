package com.moksh.kontext.presentation.screens.auth.viewmodel

// Events are one time event get triggered by viewmodel like move to next screen and something like that
sealed interface AuthScreenEvents {
    data class ShowError(val message: String) : AuthScreenEvents
    data object NavigateToHome : AuthScreenEvents
}