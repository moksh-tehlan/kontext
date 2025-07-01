package com.moksh.kontext.data.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.moksh.kontext.data.model.chat.Chat
import com.moksh.kontext.data.model.chat.ChatMessage
import com.moksh.kontext.data.model.chat.CreateChatRequest
import com.moksh.kontext.data.model.chat.SendMessageRequest
import com.moksh.kontext.data.model.chat.UpdateChatRequest
import com.moksh.kontext.domain.model.ChatDto
import com.moksh.kontext.domain.model.ChatMessageDto
import com.moksh.kontext.domain.model.CreateChatDto
import com.moksh.kontext.domain.model.MessageType
import com.moksh.kontext.domain.model.SendMessageDto
import com.moksh.kontext.domain.model.UpdateChatDto
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

fun Chat.toDto(): ChatDto {
    return ChatDto(
        id = id,
        name = name,
        projectId = projectId,
        createdAt = createdAt,
        updatedAt = updatedAt,
        isActive = isActive
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun ChatMessage.toDto(): ChatMessageDto {
    return ChatMessageDto(
        id = UUID.fromString(id),
        chatId = UUID.fromString(chatId),
        content = content,
        type = when (type) {
            "USER" -> MessageType.USER
            "ASSISTANT" -> MessageType.ASSISTANT
            else -> MessageType.USER
        },
        createdAt = LocalDateTime.parse(createdAt, DateTimeFormatter.ISO_LOCAL_DATE_TIME),
        updatedAt = LocalDateTime.parse(updatedAt, DateTimeFormatter.ISO_LOCAL_DATE_TIME),
        createdBy = createdBy,
        updatedBy = updatedBy,
        version = version.toLong(),
        isActive = isActive
    )
}

fun CreateChatDto.toRequest(): CreateChatRequest {
    return CreateChatRequest(
        name = name,
        projectId = projectId
    )
}

fun UpdateChatDto.toRequest(): UpdateChatRequest {
    return UpdateChatRequest(
        name = name
    )
}

fun SendMessageDto.toRequest(): SendMessageRequest {
    return SendMessageRequest(
        query = query
    )
}