package com.moksh.kontext.presentation.screens.chat.viewmodel

// Events are one time event get triggered by viewmodel like show error, message sent
sealed interface ChatScreenEvents {
    data class ShowError(val message: String) : ChatScreenEvents
    data object MessageSentSuccessfully : ChatScreenEvents
    data object MessageLoadedSuccessfully : ChatScreenEvents
}