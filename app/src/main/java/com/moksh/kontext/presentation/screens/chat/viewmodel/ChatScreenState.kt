package com.moksh.kontext.presentation.screens.chat.viewmodel

import com.moksh.kontext.domain.model.ChatMessageDto

data class ChatScreenState(
    val isLoading: Boolean = false,
    val messages: List<ChatMessageDto> = emptyList(),
    val currentMessage: String = "",
    val isSendingMessage: Boolean = false,
    val errorMessage: String? = null,
    val chatId: String? = null,
    val projectId: String? = null,
    val userNickname: String = "You",
    val chatName: String? = null
)