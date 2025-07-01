package com.moksh.kontext.presentation.screens.project.viewmodel

import com.moksh.kontext.domain.model.ChatDto

data class ProjectScreenState(
    val isLoading: Boolean = false,
    val chats: List<ChatDto> = emptyList(),
    val errorMessage: String? = null,
    val projectId: String? = null,
    val projectName: String = "Project Name",
    val agentInstruction: String? = null,
    val knowledgeSourceCount: Int = 0,

    // Chat operations state
    val selectedChatId: String? = null,
    val showChatOptionsMenu: Boolean = false,
    val showRenameChatDialog: Boolean = false,
    val renameChatName: String = "",
    val isRenamingChat: Boolean = false,
    val showDeleteChatDialog: Boolean = false,
    val deleteChatInfo: DeleteChatInfo? = null,
    val isDeletingChat: Boolean = false
)

data class CustomInstructionDialogState(
    val isVisible: Boolean = false,
    val isLoading: Boolean = false,
    val instruction: String = "",
    val errorMessage: String? = null
)

data class DeleteChatInfo(
    val chatId: String,
    val chatName: String
)