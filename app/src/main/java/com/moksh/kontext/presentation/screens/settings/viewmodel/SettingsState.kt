package com.moksh.kontext.presentation.screens.settings.viewmodel

data class SettingsState(
    val userEmail: String = "",
    val userTier: String = "Free",
    val isHapticFeedbackEnabled: Boolean = true,
    val isLoading: Boolean = false,
    val isLoggingOut: Boolean = false,
    val showLogoutDialog: Boolean = false,
    val showInfoDropdown: Boolean = false,
    val errorMessage: String? = null
)