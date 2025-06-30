package com.moksh.kontext.presentation.screens.chat.viewmodel

// Actions are the action performed by the user like button press, sending message
sealed interface ChatScreenActions {
    data class MessageInputChange(val message: String) : ChatScreenActions
    data object SendMessage : ChatScreenActions
    data object LoadMessages : ChatScreenActions
    data object RefreshMessages : ChatScreenActions
}