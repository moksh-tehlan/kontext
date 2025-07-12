package com.moksh.kontext.data.repository

import com.moksh.kontext.data.api.KnowledgeSourceApiService
import com.moksh.kontext.data.mapper.createWebUrlRequest
import com.moksh.kontext.data.mapper.toDto
import com.moksh.kontext.data.utils.safeCall
import com.moksh.kontext.domain.model.KnowledgeSourceDto
import com.moksh.kontext.domain.model.KnowledgeSourceStatus
import com.moksh.kontext.domain.repository.KnowledgeSourceRepository
import com.moksh.kontext.domain.utils.DataError
import com.moksh.kontext.domain.utils.Result
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KnowledgeSourceRepositoryImpl @Inject constructor(
    private val knowledgeSourceApiService: KnowledgeSourceApiService
) : KnowledgeSourceRepository {

    override suspend fun getProjectKnowledgeSources(projectId: String): Result<List<KnowledgeSourceDto>, DataError> {
        return when (val result = safeCall {
            knowledgeSourceApiService.getProjectKnowledgeSources(projectId)
        }) {
            is Result.Success -> {
                result.data.data?.let { knowledgeSources ->
                    val sortedSources = knowledgeSources.map { it.toDto() }
                        .sortedByDescending { it.updatedAt }
                    Result.Success(sortedSources)
                } ?: Result.Error(DataError.Network.EMPTY_RESPONSE)
            }

            is Result.Error -> result
        }
    }

    override suspend fun deleteKnowledgeSource(
        projectId: String,
        sourceId: String
    ): Result<Unit, DataError> {
        return when (val result = safeCall {
            knowledgeSourceApiService.deleteKnowledgeSource(projectId, sourceId)
        }) {
            is Result.Success -> {
                Result.Success(Unit)
            }

            is Result.Error -> result
        }
    }

    override suspend fun uploadFile(
        projectId: String,
        file: File
    ): Result<KnowledgeSourceDto, DataError> {
        return when (val result = safeCall {
            // Determine MIME type based on file extension
            val mimeType = when (file.extension.lowercase()) {
                "pdf" -> "application/pdf"
                "txt" -> "text/plain"
                else -> "application/octet-stream"
            }
            val requestFile = file.asRequestBody(mimeType.toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
            knowledgeSourceApiService.uploadFile(projectId, body)
        }) {
            is Result.Success -> {
                result.data.data?.let { knowledgeSource ->
                    Result.Success(knowledgeSource.toDto())
                } ?: Result.Error(DataError.Network.EMPTY_RESPONSE)
            }

            is Result.Error -> result
        }
    }

    override suspend fun createWebKnowledgeSource(
        projectId: String,
        webUrl: String
    ): Result<KnowledgeSourceDto, DataError> {
        return when (val result = safeCall {
            knowledgeSourceApiService.createWebKnowledgeSource(
                projectId,
                createWebUrlRequest(webUrl)
            )
        }) {
            is Result.Success -> {
                result.data.data?.let { knowledgeSource ->
                    Result.Success(knowledgeSource.toDto())
                } ?: Result.Error(DataError.Network.EMPTY_RESPONSE)
            }

            is Result.Error -> result
        }
    }

    override fun pollKnowledgeSourceStatus(
        projectId: String,
        knowledgeId: String
    ): Flow<Result<KnowledgeSourceStatus, DataError>> = flow {
        var shouldContinuePolling = true

        while (shouldContinuePolling) {
            when (val result = safeCall {
                knowledgeSourceApiService.getKnowledgeSourceStatus(projectId, knowledgeId)
            }) {
                is Result.Success -> {
                    result.data.data?.let { knowledgeSource ->
                        val status = when (knowledgeSource.processingStatus) {
                            "SUCCESS" -> KnowledgeSourceStatus.SUCCESS
                            "FAILURE" -> KnowledgeSourceStatus.ERROR
                            "PROCESSING" -> KnowledgeSourceStatus.PROCESSING
                            else -> KnowledgeSourceStatus.PROCESSING
                        }
                        emit(Result.Success(status))

                        // If processing is complete, stop polling
                        if (status != KnowledgeSourceStatus.PROCESSING) {
                            shouldContinuePolling = false
                        }
                    } ?: run {
                        emit(Result.Error(DataError.Network.EMPTY_RESPONSE))
                        shouldContinuePolling = false
                    }
                }

                is Result.Error -> {
                    emit(result)
                    shouldContinuePolling = false
                }
            }

            // Wait 2 seconds before next poll (shorter than the 20-second server timeout)
            if (shouldContinuePolling) {
                delay(2000)
            }
        }
    }
} 