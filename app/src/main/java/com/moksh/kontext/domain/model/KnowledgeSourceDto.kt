package com.moksh.kontext.domain.model

data class KnowledgeSourceDto(
    val id: String,
    val name: String,
    val type: KnowledgeSourceType,
    val mimeType: String? = null,
    val size: Long? = null,
    val source: String? = null,
    val createdAt: String,
    val updatedAt: String,
    val status: KnowledgeSourceStatus = KnowledgeSourceStatus.PROCESSING
)

enum class KnowledgeSourceType {
    DOCUMENT,
    WEB
}

enum class KnowledgeSourceStatus {
    PROCESSING,
    SUCCESS,
    ERROR
} 