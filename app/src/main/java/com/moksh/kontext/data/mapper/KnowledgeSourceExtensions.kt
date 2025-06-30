package com.moksh.kontext.data.mapper

import com.moksh.kontext.data.model.knowledge_source.KnowledgeSource
import com.moksh.kontext.data.model.knowledge_source.WebUrlRequest
import com.moksh.kontext.domain.model.KnowledgeSourceDto
import com.moksh.kontext.domain.model.KnowledgeSourceStatus
import com.moksh.kontext.domain.model.KnowledgeSourceType

// Data to Domain
fun KnowledgeSource.toDto(): KnowledgeSourceDto {
    return KnowledgeSourceDto(
        id = id,
        name = name,
        type = when (type.uppercase()) {
            "DOCUMENT" -> KnowledgeSourceType.DOCUMENT
            "WEB" -> KnowledgeSourceType.WEB
            else -> KnowledgeSourceType.DOCUMENT
        },
        mimeType = mimeType,
        size = size,
        source = source,
        createdAt = createdAt,
        updatedAt = updatedAt,
        status = when (processingStatus.uppercase()) {
            "SUCCESS" -> KnowledgeSourceStatus.SUCCESS
            "ERROR" -> KnowledgeSourceStatus.ERROR
            else -> KnowledgeSourceStatus.PROCESSING
        }
    )
}

// Helper function to create web URL request
fun createWebUrlRequest(webUrl: String): WebUrlRequest {
    return WebUrlRequest(webUrl = webUrl)
} 