package com.moksh.kontext.presentation.screens.home.viewmodel

// Events are one time event get triggered by viewmodel like show error, close bottom sheet
sealed interface HomeScreenEvents {
    data class ShowError(val message: String) : HomeScreenEvents
    data object ProjectCreatedSuccessfully : HomeScreenEvents
    data object ProjectDeletedSuccessfully : HomeScreenEvents
    data object CloseCreateProjectBottomSheet : HomeScreenEvents
}
