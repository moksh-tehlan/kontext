package com.moksh.kontext.data.model.knowledge_source

import kotlinx.serialization.Serializable

@Serializable
data class KnowledgeSource(
    val id: String,
    val createdAt: String,
    val updatedAt: String,
    val createdBy: String,
    val updatedBy: String,
    val version: Int,
    val isActive: Boolean,
    val name: String,
    val type: String,
    val mimeType: String? = null,
    val size: Long? = null,
    val source: String? = null,
    val processingStatus: String
)

@Serializable
data class WebUrlRequest(
    val webUrl: String
) 