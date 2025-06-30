package com.moksh.kontext.domain.repository

import com.moksh.kontext.domain.model.ChatDto
import com.moksh.kontext.domain.model.ChatMessageDto
import com.moksh.kontext.domain.model.CreateChatDto
import com.moksh.kontext.domain.model.SendMessageDto
import com.moksh.kontext.domain.utils.DataError
import com.moksh.kontext.domain.utils.Result

interface ChatRepository {

    suspend fun createChat(createChatDto: CreateChatDto): Result<ChatDto, DataError>

    suspend fun sendMessage(
        chatId: String,
        sendMessageDto: SendMessageDto
    ): Result<String, DataError>

    suspend fun getChatHistory(chatId: String): Result<List<ChatMessageDto>, DataError>

    suspend fun getProjectChats(projectId: String): Result<List<ChatDto>, DataError>
}