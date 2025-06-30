package com.moksh.kontext.presentation.screens.project.viewmodel

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moksh.kontext.domain.repository.ChatRepository
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
            // Load chats when screen starts
            loadChats()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            ProjectScreenState(
                projectId = projectId,
                projectName = projectName
            )
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
}