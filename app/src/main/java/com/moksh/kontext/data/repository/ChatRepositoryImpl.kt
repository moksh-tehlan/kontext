package com.moksh.kontext.data.repository

import com.moksh.kontext.data.api.ChatApiService
import com.moksh.kontext.data.mapper.toDto
import com.moksh.kontext.data.mapper.toRequest
import com.moksh.kontext.data.utils.safeCall
import com.moksh.kontext.domain.model.ChatDto
import com.moksh.kontext.domain.model.ChatMessageDto
import com.moksh.kontext.domain.model.CreateChatDto
import com.moksh.kontext.domain.model.SendMessageDto
import com.moksh.kontext.domain.repository.ChatRepository
import com.moksh.kontext.domain.utils.DataError
import com.moksh.kontext.domain.utils.Result
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepositoryImpl @Inject constructor(
    private val chatApiService: ChatApiService
) : ChatRepository {

    override suspend fun createChat(createChatDto: CreateChatDto): Result<ChatDto, DataError> {
        return when (val result =
            safeCall { chatApiService.createChat(createChatDto.toRequest()) }) {
            is Result.Success -> {
                result.data.data?.let { chat ->
                    Result.Success(chat.toDto())
                } ?: Result.Error(DataError.Network.EMPTY_RESPONSE)
            }

            is Result.Error -> result
        }
    }

    override suspend fun sendMessage(
        chatId: String,
        sendMessageDto: SendMessageDto
    ): Result<String, DataError> {
        return when (val result = safeCall {
            chatApiService.sendMessage(chatId, sendMessageDto.toRequest())
        }) {
            is Result.Success -> {
                result.data.data?.let { response ->
                    Result.Success(response)
                } ?: Result.Error(DataError.Network.EMPTY_RESPONSE)
            }

            is Result.Error -> result
        }
    }

    override suspend fun getChatHistory(chatId: String): Result<List<ChatMessageDto>, DataError> {
        return when (val result = safeCall { chatApiService.getChatHistory(chatId) }) {
            is Result.Success -> {
                result.data.data?.let { messages ->
                    Result.Success(messages.map { it.toDto() })
                } ?: Result.Error(DataError.Network.EMPTY_RESPONSE)
            }

            is Result.Error -> result
        }
    }

    override suspend fun getProjectChats(projectId: String): Result<List<ChatDto>, DataError> {
        return when (val result = safeCall { chatApiService.getProjectChats(projectId) }) {
            is Result.Success -> {
                result.data.data?.let { chats ->
                    val sortedChats = chats.map { it.toDto() }
                        .sortedByDescending { it.updatedAt }
                    Result.Success(sortedChats)
                } ?: Result.Error(DataError.Network.EMPTY_RESPONSE)
            }

            is Result.Error -> result
        }
    }
}