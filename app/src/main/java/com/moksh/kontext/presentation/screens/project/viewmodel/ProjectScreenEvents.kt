package com.moksh.kontext.presentation.screens.project.viewmodel

sealed class ProjectScreenEvents {
    data class ShowError(val message: String) : ProjectScreenEvents()
    data class NavigateToChat(val projectId: String, val chatId: String) : ProjectScreenEvents()
    data object ChatsLoadedSuccessfully : ProjectScreenEvents()
    data object CustomInstructionSavedSuccessfully : ProjectScreenEvents()
    data object CloseCustomInstructionDialog : ProjectScreenEvents()

    // Chat operations events
    data object ChatRenamedSuccessfully : ProjectScreenEvents()
    data object ChatDeletedSuccessfully : ProjectScreenEvents()
}