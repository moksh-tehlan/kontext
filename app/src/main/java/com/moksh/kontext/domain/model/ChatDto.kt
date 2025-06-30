package com.moksh.kontext.domain.model

data class ChatDto(
    val id: String,
    val name: String,
    val projectId: String,
    val createdAt: String,
    val updatedAt: String,
    val isActive: Boolean
)

data class CreateChatDto(
    val name: String,
    val projectId: String
)

data class SendMessageDto(
    val query: String
)