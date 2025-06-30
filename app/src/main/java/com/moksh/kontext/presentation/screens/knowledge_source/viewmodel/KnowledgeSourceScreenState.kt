package com.moksh.kontext.presentation.screens.knowledge_source.viewmodel

import com.moksh.kontext.domain.model.KnowledgeSourceDto

data class KnowledgeSourceScreenState(
    val isLoading: Boolean = false,
    val knowledgeSources: List<KnowledgeSourceDto> = emptyList(),
    val errorMessage: String? = null,
    val projectId: String? = null,
    val projectName: String = "Project Knowledge"
)

data class AddContentBottomSheetState(
    val isVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class WebUrlDialogState(
    val isVisible: Boolean = false,
    val isLoading: Boolean = false,
    val url: String = "",
    val errorMessage: String? = null
) 