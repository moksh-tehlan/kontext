package com.moksh.kontext.presentation.screens.knowledge_source.viewmodel

sealed class KnowledgeSourceScreenEvents {
    data class ShowError(val message: String) : KnowledgeSourceScreenEvents()
    data object KnowledgeSourcesLoadedSuccessfully : KnowledgeSourceScreenEvents()
    data object KnowledgeSourceAddedSuccessfully : KnowledgeSourceScreenEvents()
    data object KnowledgeSourceDeletedSuccessfully : KnowledgeSourceScreenEvents()
    data object CloseAddContentBottomSheet : KnowledgeSourceScreenEvents()
    data object CloseWebUrlDialog : KnowledgeSourceScreenEvents()
    data object OpenFilePicker : KnowledgeSourceScreenEvents()
} 