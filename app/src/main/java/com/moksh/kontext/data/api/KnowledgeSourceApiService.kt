package com.moksh.kontext.data.api

import com.moksh.kontext.data.model.ApiResponse
import com.moksh.kontext.data.model.knowledge_source.KnowledgeSource
import com.moksh.kontext.data.model.knowledge_source.WebUrlRequest
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface KnowledgeSourceApiService {

    @GET("api/v1/projects/{projectId}/knowledge")
    suspend fun getProjectKnowledgeSources(@Path("projectId") projectId: String): Response<ApiResponse<List<KnowledgeSource>>>

    @DELETE("api/v1/projects/{projectId}/knowledge/{sourceId}")
    suspend fun deleteKnowledgeSource(
        @Path("projectId") projectId: String,
        @Path("sourceId") sourceId: String
    ): Response<ApiResponse<Unit>>

    @Multipart
    @POST("api/v1/projects/{projectId}/knowledge/upload")
    suspend fun uploadFile(
        @Path("projectId") projectId: String,
        @Part file: MultipartBody.Part
    ): Response<ApiResponse<KnowledgeSource>>

    @POST("api/v1/projects/{projectId}/knowledge/web")
    suspend fun createWebKnowledgeSource(
        @Path("projectId") projectId: String,
        @Body request: WebUrlRequest
    ): Response<ApiResponse<KnowledgeSource>>

    @GET("api/v1/projects/{projectId}/knowledge/{knowledgeId}/status")
    suspend fun getKnowledgeSourceStatus(
        @Path("projectId") projectId: String,
        @Path("knowledgeId") knowledgeId: String
    ): Response<ApiResponse<KnowledgeSource>>
} 