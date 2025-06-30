package com.moksh.kontext.presentation.screens.chat.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moksh.kontext.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
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
    @ApplicationContext private val context: Context,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _chatState = MutableStateFlow(ChatScreenState())
    val chatState = _chatState.asStateFlow()
        .onStart {
            // Load user nickname when screen starts
            loadUserNickname()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            ChatScreenState()
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

    fun initializeChat(projectId: String, chatId: String? = null) {
        _chatState.update {
            it.copy(
                projectId = projectId,
                chatId = chatId
            )
        }

        if (chatId != null) {
            loadMessages()
        }
    }

    private fun sendMessage() {
        viewModelScope.launch {
            val currentState = _chatState.value

            if (currentState.currentMessage.isBlank()) {
                return@launch
            }

            _chatState.update {
                it.copy(
                    isSendingMessage = true,
                    errorMessage = null
                )
            }

            try {
                // TODO: Implement actual API call to send message
                // For now, just clear the input and show success
                _chatState.update {
                    it.copy(
                        isSendingMessage = false,
                        currentMessage = ""
                    )
                }

                _chatEvents.emit(ChatScreenEvents.MessageSentSuccessfully)

                // TODO: Add the sent message to the messages list
                // TODO: Handle response from backend

            } catch (e: Exception) {
                _chatState.update {
                    it.copy(
                        isSendingMessage = false,
                        errorMessage = e.message ?: "Failed to send message"
                    )
                }
                _chatEvents.emit(ChatScreenEvents.ShowError("Failed to send message"))
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

            try {
                // TODO: Implement actual API call to load messages
                // For now, just set loading to false
                _chatState.update {
                    it.copy(
                        isLoading = false,
                        messages = emptyList() // TODO: Replace with actual messages from API
                    )
                }

                _chatEvents.emit(ChatScreenEvents.MessageLoadedSuccessfully)

            } catch (e: Exception) {
                _chatState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Failed to load messages"
                    )
                }
                _chatEvents.emit(ChatScreenEvents.ShowError("Failed to load messages"))
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