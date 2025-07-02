package com.moksh.kontext.presentation.screens.settings.viewmodel

/**
 * Events that are triggered once by the ViewModel to communicate with the UI
 */
sealed interface SettingsEvents {
    data object NavigateBack : SettingsEvents
    data object NavigateToProfile : SettingsEvents
    data object NavigateToBilling : SettingsEvents
    data object NavigateToUpgrade : SettingsEvents
    data object NavigateToAuth : SettingsEvents
    data class ShowError(val message: String) : SettingsEvents
    data class ShowSuccess(val message: String) : SettingsEvents
    data class ShowInfo(val message: String) : SettingsEvents
    data class OpenExternalLink(val url: String) : SettingsEvents
}