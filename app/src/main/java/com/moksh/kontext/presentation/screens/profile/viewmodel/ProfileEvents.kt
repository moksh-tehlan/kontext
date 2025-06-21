package com.moksh.kontext.presentation.screens.profile.viewmodel

/**
 * Events that are triggered once by the ViewModel to communicate with the UI
 */
sealed interface ProfileEvents {
    data object NavigateBack : ProfileEvents
    data object NavigateToAuth : ProfileEvents
    data class ShowError(val message: String) : ProfileEvents
    data class ShowSuccess(val message: String) : ProfileEvents
}