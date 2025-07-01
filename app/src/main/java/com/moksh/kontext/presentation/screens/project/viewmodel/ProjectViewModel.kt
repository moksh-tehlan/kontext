package com.moksh.kontext.presentation.screens.project.viewmodel

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moksh.kontext.domain.model.UpdateChatDto
import com.moksh.kontext.domain.model.UpdateProjectDto
import com.moksh.kontext.domain.repository.ChatRepository
import com.moksh.kontext.domain.repository.KnowledgeSourceRepository
import com.moksh.kontext.domain.repository.ProjectRepository
import com.moksh.kontext.domain.utils.Result
import com.moksh.kontext.presentation.core.utils.asUiText
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
class ProjectViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val chatRepository: ChatRepository,
    private val projectRepository: ProjectRepository,
    private val knowledgeSourceRepository: KnowledgeSourceRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val projectId: String = checkNotNull(savedStateHandle["projectId"])
    private val projectName: String = savedStateHandle["projectName"] ?: "Project"

    private val _projectState = MutableStateFlow(
        ProjectScreenState(
            projectId = projectId,
            projectName = projectName
        )
    )
    val projectState = _projectState.asStateFlow()
        .onStart {
            // Load project details and chats when screen starts
            loadProjectDetails()
            loadChats()
            loadKnowledgeSourceCount()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            ProjectScreenState(
                projectId = projectId,
                projectName = projectName
            )
        )

    private val _customInstructionDialogState = MutableStateFlow(CustomInstructionDialogState())
    val customInstructionDialogState = _customInstructionDialogState.asStateFlow()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            CustomInstructionDialogState()
        )

    private val _projectEvents = MutableSharedFlow<ProjectScreenEvents>()
    val projectEvents = _projectEvents.asSharedFlow()

    fun onAction(action: ProjectScreenActions) {
        when (action) {
            is ProjectScreenActions.LoadChats -> {
                loadChats()
            }

            is ProjectScreenActions.RefreshChats -> {
                loadChats()
            }

            is ProjectScreenActions.NavigateToChat -> {
                navigateToChat(action.chatId)
            }

            is ProjectScreenActions.CreateNewChat -> {
                createNewChat()
            }

            is ProjectScreenActions.ShowCustomInstructionDialog -> {
                val currentInstruction = _projectState.value.agentInstruction ?: ""
                _customInstructionDialogState.update {
                    it.copy(
                        isVisible = true,
                        isLoading = false,
                        instruction = currentInstruction,
                        errorMessage = null
                    )
                }
            }

            is ProjectScreenActions.HideCustomInstructionDialog -> {
                _customInstructionDialogState.update {
                    it.copy(
                        isVisible = false,
                        isLoading = false,
                        instruction = "",
                        errorMessage = null
                    )
                }
            }

            is ProjectScreenActions.InstructionChange -> {
                _customInstructionDialogState.update {
                    it.copy(
                        instruction = action.instruction,
                        errorMessage = null
                    )
                }
            }

            is ProjectScreenActions.SaveCustomInstruction -> {
                saveCustomInstruction()
            }

            // Chat operations
            is ProjectScreenActions.ShowChatOptionsMenu -> {
                _projectState.update {
                    it.copy(
                        selectedChatId = action.chatId,
                        showChatOptionsMenu = true
                    )
                }
            }

            is ProjectScreenActions.HideChatOptionsMenu -> {
                _projectState.update {
                    it.copy(
                        showChatOptionsMenu = false
                    )
                }
            }

            is ProjectScreenActions.ShowRenameChatDialog -> {
                _projectState.update {
                    it.copy(
                        selectedChatId = action.chatId,
                        showChatOptionsMenu = false,
                        showRenameChatDialog = true,
                        renameChatName = action.currentName
                    )
                }
            }

            is ProjectScreenActions.HideRenameChatDialog -> {
                _projectState.update {
                    it.copy(
                        selectedChatId = null,
                        showRenameChatDialog = false,
                        renameChatName = "",
                        isRenamingChat = false
                    )
                }
            }

            is ProjectScreenActions.RenameChatNameChange -> {
                _projectState.update {
                    it.copy(renameChatName = action.name)
                }
            }

            is ProjectScreenActions.ConfirmRenameChat -> {
                renameChat()
            }

            is ProjectScreenActions.ShowDeleteChatDialog -> {
                _projectState.update {
                    it.copy(
                        showChatOptionsMenu = false,
                        showDeleteChatDialog = true,
                        deleteChatInfo = DeleteChatInfo(
                            chatId = action.chatId,
                            chatName = action.chatName
                        )
                    )
                }
            }

            is ProjectScreenActions.HideDeleteChatDialog -> {
                _projectState.update {
                    it.copy(
                        showDeleteChatDialog = false,
                        deleteChatInfo = null,
                        isDeletingChat = false
                    )
                }
            }

            is ProjectScreenActions.ConfirmDeleteChat -> {
                deleteChat(action.chatId)
            }
        }
    }

    private fun loadProjectDetails() {
        viewModelScope.launch {
            when (val result = projectRepository.getProject(projectId)) {
                is Result.Success -> {
                    _projectState.update {
                        it.copy(
                            agentInstruction = result.data.agentInstruction,
                            projectName = result.data.name
                        )
                    }
                }

                is Result.Error -> {
                    // Handle error silently or show a subtle error
                    // Not updating error message as this is a background operation
                }
            }
        }
    }

    private fun loadChats() {
        viewModelScope.launch {

            _projectState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            when (val result = chatRepository.getProjectChats(projectId)) {
                is Result.Success -> {
                    _projectState.update {
                        it.copy(
                            isLoading = false,
                            chats = result.data
                        )
                    }

                    _projectEvents.emit(ProjectScreenEvents.ChatsLoadedSuccessfully)
                }

                is Result.Error -> {
                    _projectState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.error.asUiText().asString(context)
                        )
                    }
                    _projectEvents.emit(
                        ProjectScreenEvents.ShowError(
                            result.error.asUiText().asString(context)
                        )
                    )
                }
            }
        }
    }

    private fun navigateToChat(chatId: String) {
        viewModelScope.launch {
            _projectEvents.emit(ProjectScreenEvents.NavigateToChat(projectId, chatId))
        }
    }

    private fun createNewChat() {
        viewModelScope.launch {
            _projectEvents.emit(ProjectScreenEvents.NavigateToChat(projectId, "new_chat"))
        }
    }

    private fun loadKnowledgeSourceCount() {
        viewModelScope.launch {
            when (val result = knowledgeSourceRepository.getProjectKnowledgeSources(projectId)) {
                is Result.Success -> {
                    _projectState.update {
                        it.copy(knowledgeSourceCount = result.data.size)
                    }
                }

                is Result.Error -> {
                    // Handle error silently - knowledge source count is not critical
                    // Keep default count of 0
                }
            }
        }
    }

    private fun saveCustomInstruction() {
        viewModelScope.launch {
            val currentInstruction = _customInstructionDialogState.value.instruction

            if (currentInstruction.isBlank()) {
                _customInstructionDialogState.update {
                    it.copy(errorMessage = "Instruction cannot be empty")
                }
                return@launch
            }

            _customInstructionDialogState.update {
                it.copy(isLoading = true, errorMessage = null)
            }

            when (val result = projectRepository.updateProject(
                projectId = projectId,
                updateProjectDto = UpdateProjectDto(agentInstruction = currentInstruction)
            )) {
                is Result.Success -> {
                    _customInstructionDialogState.update {
                        it.copy(
                            isLoading = false,
                            isVisible = false,
                            instruction = "",
                            errorMessage = null
                        )
                    }

                    // Update the project state with the saved instruction
                    _projectState.update {
                        it.copy(agentInstruction = currentInstruction)
                    }

                    _projectEvents.emit(ProjectScreenEvents.CustomInstructionSavedSuccessfully)
                }

                is Result.Error -> {
                    val errorMessage = result.error.asUiText().asString(context)
                    _customInstructionDialogState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = errorMessage
                        )
                    }
                    _projectEvents.emit(ProjectScreenEvents.ShowError(errorMessage))
                }
            }
        }
    }

    private fun renameChat() {
        viewModelScope.launch {
            val currentState = _projectState.value
            val chatId = currentState.selectedChatId ?: return@launch
            val newName = currentState.renameChatName.trim()

            if (newName.isBlank()) {
                _projectEvents.emit(ProjectScreenEvents.ShowError("Chat name cannot be empty"))
                return@launch
            }

            _projectState.update {
                it.copy(isRenamingChat = true)
            }

            when (val result = chatRepository.updateChat(chatId, UpdateChatDto(name = newName))) {
                is Result.Success -> {
                    // Update the chat in the local list
                    _projectState.update { state ->
                        state.copy(
                            isRenamingChat = false,
                            showRenameChatDialog = false,
                            selectedChatId = null,
                            renameChatName = "",
                            chats = state.chats.map { chat ->
                                if (chat.id == chatId) {
                                    chat.copy(name = newName)
                                } else {
                                    chat
                                }
                            }
                        )
                    }

                    _projectEvents.emit(ProjectScreenEvents.ChatRenamedSuccessfully)
                }

                is Result.Error -> {
                    _projectState.update {
                        it.copy(isRenamingChat = false)
                    }

                    val errorMessage = result.error.asUiText().asString(context)
                    _projectEvents.emit(ProjectScreenEvents.ShowError("Failed to rename chat: $errorMessage"))
                }
            }
        }
    }

    private fun deleteChat(chatId: String) {
        viewModelScope.launch {
            _projectState.update {
                it.copy(isDeletingChat = true)
            }

            when (val result = chatRepository.deleteChat(chatId)) {
                is Result.Success -> {
                    // Remove the chat from the local list
                    _projectState.update { state ->
                        state.copy(
                            isDeletingChat = false,
                            showDeleteChatDialog = false,
                            deleteChatInfo = null,
                            chats = state.chats.filter { it.id != chatId }
                        )
                    }

                    _projectEvents.emit(ProjectScreenEvents.ChatDeletedSuccessfully)
                }

                is Result.Error -> {
                    _projectState.update {
                        it.copy(isDeletingChat = false)
                    }

                    val errorMessage = result.error.asUiText().asString(context)
                    _projectEvents.emit(ProjectScreenEvents.ShowError("Failed to delete chat: $errorMessage"))
                }
            }
        }
    }
}