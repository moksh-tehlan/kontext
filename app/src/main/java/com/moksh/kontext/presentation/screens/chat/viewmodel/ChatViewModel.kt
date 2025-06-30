package com.moksh.kontext.presentation.screens.chat.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moksh.kontext.domain.model.CreateChatDto
import com.moksh.kontext.domain.model.MessageType
import com.moksh.kontext.domain.model.SendMessageDto
import com.moksh.kontext.domain.repository.ChatRepository
import com.moksh.kontext.domain.repository.UserRepository
import com.moksh.kontext.domain.utils.DataError
import com.moksh.kontext.domain.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val projectId: String = checkNotNull(savedStateHandle["projectId"])
    private val chatId: String? = savedStateHandle["chatId"]

    private val _chatState = MutableStateFlow(
        ChatScreenState(
            projectId = projectId,
            chatId = chatId
        )
    )
    val chatState = _chatState.asStateFlow()
        .onStart {
            // Load user nickname when screen starts
            loadUserNickname()
            // Initialize chat
            initializeChat()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            ChatScreenState(
                projectId = projectId,
                chatId = chatId
            )
        )

    private val _chatEvents = MutableSharedFlow<ChatScreenEvents>()
    val chatEvents = _chatEvents.asSharedFlow()

    fun onAction(action: ChatScreenActions) {
        when (action) {
            is ChatScreenActions.MessageInputChange -> {
                _chatState.update {
                    it.copy(
                        currentMessage = action.message,
                        errorMessage = null
                    )
                }
            }

            is ChatScreenActions.SendMessage -> {
                sendMessage()
            }

            is ChatScreenActions.LoadMessages -> {
                loadMessages()
            }

            is ChatScreenActions.RefreshMessages -> {
                loadMessages()
            }
        }
    }

    private fun initializeChat() {
        if (chatId != null && chatId != "new_chat") {
            loadMessages()
        } else {
            // Create new chat
            createNewChat()
        }
    }

    private fun sendMessage() {
        viewModelScope.launch {
            val currentState = _chatState.value

            if (currentState.currentMessage.isBlank()) {
                return@launch
            }

            if (currentState.chatId == null) {
                _chatState.update {
                    it.copy(
                        errorMessage = "No active chat session"
                    )
                }
                return@launch
            }

            val userMessage = currentState.currentMessage

            // Create user message and add it to the list immediately
            val userMessageDto = com.moksh.kontext.domain.model.ChatMessageDto(
                id = java.util.UUID.randomUUID(),
                chatId = java.util.UUID.fromString(currentState.chatId),
                content = userMessage,
                type = MessageType.USER,
                createdAt = java.time.LocalDateTime.now(),
                updatedAt = java.time.LocalDateTime.now(),
                createdBy = "user",
                updatedBy = "user",
                version = 0L,
                isActive = true
            )

            // Update state: clear input, add user message, set sending state
            _chatState.update {
                it.copy(
                    currentMessage = "",
                    isSendingMessage = true,
                    errorMessage = null,
                    messages = it.messages + userMessageDto
                )
            }

            when (val result = chatRepository.sendMessage(
                chatId = currentState.chatId,
                sendMessageDto = SendMessageDto(query = userMessage)
            )) {
                is Result.Success -> {
                    _chatState.update {
                        it.copy(isSendingMessage = false)
                    }

                    _chatEvents.emit(ChatScreenEvents.MessageSentSuccessfully)

                    // Reload messages to get the assistant response
                    loadMessages()
                }

                is Result.Error -> {
                    val errorMessage = when (result.error) {
                        DataError.Network.UNAUTHORIZED -> "Session expired. Please login again."
                        DataError.Network.NO_INTERNET -> "No internet connection"
                        DataError.Network.SERVER_ERROR -> "Server error occurred"
                        else -> "Failed to send message"
                    }

                    // Remove the user message if send failed
                    _chatState.update {
                        it.copy(
                            isSendingMessage = false,
                            errorMessage = errorMessage,
                            messages = it.messages.filter { msg -> msg.id != userMessageDto.id }
                        )
                    }
                    _chatEvents.emit(ChatScreenEvents.ShowError(errorMessage))
                }
            }
        }
    }

    private fun loadMessages() {
        viewModelScope.launch {
            val currentState = _chatState.value

            if (currentState.chatId == null) {
                return@launch
            }

            _chatState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            when (val result = chatRepository.getChatHistory(currentState.chatId)) {
                is Result.Success -> {
                    _chatState.update {
                        it.copy(
                            isLoading = false,
                            messages = result.data
                        )
                    }

                    _chatEvents.emit(ChatScreenEvents.MessageLoadedSuccessfully)
                }

                is Result.Error -> {
                    val errorMessage = when (result.error) {
                        DataError.Network.UNAUTHORIZED -> "Session expired. Please login again."
                        DataError.Network.NO_INTERNET -> "No internet connection"
                        DataError.Network.SERVER_ERROR -> "Server error occurred"
                        else -> "Failed to load messages"
                    }

                    _chatState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = errorMessage
                        )
                    }
                    _chatEvents.emit(ChatScreenEvents.ShowError(errorMessage))
                }
            }
        }
    }

    private fun createNewChat() {
        viewModelScope.launch {
            _chatState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            when (val result = chatRepository.createChat(
                CreateChatDto(
                    name = "New Chat",
                    projectId = projectId
                )
            )) {
                is Result.Success -> {
                    _chatState.update {
                        it.copy(
                            isLoading = false,
                            chatId = result.data.id
                        )
                    }
                }

                is Result.Error -> {
                    val errorMessage = when (result.error) {
                        DataError.Network.UNAUTHORIZED -> "Session expired. Please login again."
                        DataError.Network.NO_INTERNET -> "No internet connection"
                        DataError.Network.SERVER_ERROR -> "Server error occurred"
                        else -> "Failed to create chat"
                    }

                    _chatState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = errorMessage
                        )
                    }
                    _chatEvents.emit(ChatScreenEvents.ShowError(errorMessage))
                }
            }
        }
    }

    private fun loadUserNickname() {
        viewModelScope.launch {
            val cachedUser = userRepository.getCachedUser()
            if (cachedUser != null) {
                _chatState.update {
                    it.copy(userNickname = cachedUser.nickname.ifEmpty { "You" })
                }
            }
        }
    }
}