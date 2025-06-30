package com.moksh.kontext.data.api

import com.moksh.kontext.data.model.ApiResponse
import com.moksh.kontext.data.model.project.CreateProjectRequest
import com.moksh.kontext.data.model.project.Project
import com.moksh.kontext.data.model.project.UpdateProjectRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProjectApiService {

    @GET("api/v1/projects")
    suspend fun getAllProjects(): Response<ApiResponse<List<Project>>>

    @GET("api/v1/projects/{projectId}")
    suspend fun getProject(@Path("projectId") projectId: String): Response<ApiResponse<Project>>

    @POST("api/v1/projects")
    suspend fun createProject(@Body createProjectRequest: CreateProjectRequest): Response<ApiResponse<Project>>

    @PUT("api/v1/projects/{projectId}")
    suspend fun updateProject(
        @Path("projectId") projectId: String,
        @Body updateProjectRequest: UpdateProjectRequest
    ): Response<ApiResponse<Project>>

    @DELETE("api/v1/projects/{projectId}")
    suspend fun deleteProject(@Path("projectId") projectId: String): Response<ApiResponse<Unit>>
}