package com.moksh.kontext.presentation.screens.project.viewmodel

import com.moksh.kontext.domain.model.ChatDto

data class ProjectScreenState(
    val isLoading: Boolean = false,
    val chats: List<ChatDto> = emptyList(),
    val errorMessage: String? = null,
    val projectId: String? = null,
    val projectName: String = "Project Name"
)