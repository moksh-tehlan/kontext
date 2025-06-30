package com.moksh.kontext.domain.model

import java.time.LocalDateTime
import java.util.UUID

data class ChatMessageDto(
    val id: UUID,
    val chatId: UUID,
    val content: String,
    val type: MessageType,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val createdBy: String,
    val updatedBy: String,
    val version: Long,
    val isActive: Boolean
)

enum class MessageType {
    USER,
    ASSISTANT
}