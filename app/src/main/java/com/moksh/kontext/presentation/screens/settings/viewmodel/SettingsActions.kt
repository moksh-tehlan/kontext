package com.moksh.kontext.presentation.screens.settings.viewmodel

/**
 * Actions that represent user interactions with the Settings screen
 */
sealed interface SettingsActions {
    data object NavigateBack : SettingsActions
    data object ShowInfo : SettingsActions
    data object DismissInfoDropdown : SettingsActions
    data object NavigateToProfile : SettingsActions
    data object NavigateToBilling : SettingsActions
    data object NavigateToUpgrade : SettingsActions
    data object ToggleHapticFeedback : SettingsActions
    data object ShowLogoutDialog : SettingsActions
    data object ConfirmLogout : SettingsActions
    data object DismissLogoutDialog : SettingsActions
    data object OnConsumerTermsClick : SettingsActions
    data object OnPrivacyPolicyClick : SettingsActions
    data object OnHelpSupportClick : SettingsActions
    data object ClearError : SettingsActions
}