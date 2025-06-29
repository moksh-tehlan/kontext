package com.moksh.kontext.data.api

import com.moksh.kontext.data.model.ApiResponse
import com.moksh.kontext.data.model.user.UpdateUserRequest
import com.moksh.kontext.data.model.user.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserApiService {

    @GET("api/v1/users/me")
    suspend fun getCurrentUser(): Response<ApiResponse<User>>

    @PUT("api/v1/users/{userId}")
    suspend fun updateUser(
        @Path("userId") userId: String,
        @Body updateUserRequest: UpdateUserRequest
    ): Response<ApiResponse<User>>

    @DELETE("api/v1/users/")
    suspend fun deleteCurrentUser(): Response<ApiResponse<Unit>>
}