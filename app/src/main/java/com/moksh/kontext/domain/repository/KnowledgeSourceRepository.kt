package com.moksh.kontext.domain.repository

import com.moksh.kontext.domain.model.KnowledgeSourceDto
import com.moksh.kontext.domain.model.KnowledgeSourceStatus
import com.moksh.kontext.domain.utils.DataError
import com.moksh.kontext.domain.utils.Result
import kotlinx.coroutines.flow.Flow
import java.io.File

interface KnowledgeSourceRepository {

    suspend fun getProjectKnowledgeSources(projectId: String): Result<List<KnowledgeSourceDto>, DataError>

    suspend fun deleteKnowledgeSource(projectId: String, sourceId: String): Result<Unit, DataError>

    suspend fun uploadFile(
        projectId: String,
        file: File
    ): Result<KnowledgeSourceDto, DataError>

    suspend fun createWebKnowledgeSource(
        projectId: String,
        webUrl: String
    ): Result<KnowledgeSourceDto, DataError>

    fun pollKnowledgeSourceStatus(
        projectId: String,
        knowledgeId: String
    ): Flow<Result<KnowledgeSourceStatus, DataError>>
} 