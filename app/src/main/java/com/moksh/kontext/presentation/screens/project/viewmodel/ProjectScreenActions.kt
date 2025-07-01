package com.moksh.kontext.presentation.screens.project.viewmodel

sealed class ProjectScreenActions {
    data object LoadChats : ProjectScreenActions()
    data object RefreshChats : ProjectScreenActions()
    data class NavigateToChat(val chatId: String) : ProjectScreenActions()
    data object CreateNewChat : ProjectScreenActions()
    data object ShowCustomInstructionDialog : ProjectScreenActions()
    data object HideCustomInstructionDialog : ProjectScreenActions()
    data class InstructionChange(val instruction: String) : ProjectScreenActions()
    data object SaveCustomInstruction : ProjectScreenActions()

    // Chat operations
    data class ShowChatOptionsMenu(val chatId: String) : ProjectScreenActions()
    data object HideChatOptionsMenu : ProjectScreenActions()
    data class ShowRenameChatDialog(val chatId: String, val currentName: String) :
        ProjectScreenActions()

    data object HideRenameChatDialog : ProjectScreenActions()
    data class RenameChatNameChange(val name: String) : ProjectScreenActions()
    data object ConfirmRenameChat : ProjectScreenActions()
    data class ShowDeleteChatDialog(val chatId: String, val chatName: String) :
        ProjectScreenActions()

    data object HideDeleteChatDialog : ProjectScreenActions()
    data class ConfirmDeleteChat(val chatId: String) : ProjectScreenActions()
}