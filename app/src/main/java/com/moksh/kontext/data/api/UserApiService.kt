package com.moksh.kontext.data.api

import com.moksh.kontext.data.model.ApiResponse
import com.moksh.kontext.data.model.user.User
import retrofit2.Response
import retrofit2.http.GET

interface UserApiService {

    @GET("api/v1/users/me")
    suspend fun getCurrentUser(): Response<ApiResponse<User>>
}