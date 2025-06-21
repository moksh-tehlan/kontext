package com.moksh.kontext.presentation.screens.settings.viewmodel

/**
 * Actions that represent user interactions with the Settings screen
 */
sealed interface SettingsActions {
    data object NavigateBack : SettingsActions
    data object ShowInfo : SettingsActions
    data object NavigateToProfile : SettingsActions
    data object NavigateToBilling : SettingsActions
    data object NavigateToUpgrade : SettingsActions
    data object ToggleHapticFeedback : SettingsActions
    data object Logout : SettingsActions
    data object ClearError : SettingsActions
}