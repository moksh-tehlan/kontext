package com.moksh.kontext.presentation.screens.project.viewmodel

sealed class ProjectScreenActions {
    data object LoadChats : ProjectScreenActions()
    data object RefreshChats : ProjectScreenActions()
    data class NavigateToChat(val chatId: String) : ProjectScreenActions()
    data object CreateNewChat : ProjectScreenActions()
}