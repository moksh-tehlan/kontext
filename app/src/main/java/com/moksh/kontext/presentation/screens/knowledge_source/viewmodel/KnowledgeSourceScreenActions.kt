package com.moksh.kontext.presentation.screens.knowledge_source.viewmodel

import java.io.File

sealed class KnowledgeSourceScreenActions {
    data object LoadKnowledgeSources : KnowledgeSourceScreenActions()
    data object RefreshKnowledgeSources : KnowledgeSourceScreenActions()
    data object ShowAddContentBottomSheet : KnowledgeSourceScreenActions()
    data object HideAddContentBottomSheet : KnowledgeSourceScreenActions()
    data object UploadFromDevice : KnowledgeSourceScreenActions()
    data class UploadFile(val file: File) : KnowledgeSourceScreenActions()
    data object ShowWebUrlDialog : KnowledgeSourceScreenActions()
    data object HideWebUrlDialog : KnowledgeSourceScreenActions()
    data class UrlChange(val url: String) : KnowledgeSourceScreenActions()
    data object AddWebUrl : KnowledgeSourceScreenActions()
    data class DeleteKnowledgeSource(val sourceId: String) : KnowledgeSourceScreenActions()
    data class StartPollingStatus(val knowledgeId: String) : KnowledgeSourceScreenActions()
    data class StopPollingStatus(val knowledgeId: String) : KnowledgeSourceScreenActions()
} 