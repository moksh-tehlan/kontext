package com.moksh.kontext.data.api

import com.moksh.kontext.data.model.ApiResponse
import com.moksh.kontext.data.model.auth.AuthResponse
import com.moksh.kontext.data.model.auth.GoogleLoginRequest
import com.moksh.kontext.data.model.auth.LoginRequest
import com.moksh.kontext.data.model.auth.RefreshTokenRequest
import com.moksh.kontext.data.model.auth.SendOtpRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("api/v1/auth/send-otp")
    suspend fun sendOtp(
        @Body request: SendOtpRequest
    ): Response<ApiResponse<Unit>>

    @POST("api/v1/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<ApiResponse<AuthResponse>>

    @POST("api/v1/auth/refresh")
    suspend fun refreshToken(
        @Body request: RefreshTokenRequest
    ): Response<ApiResponse<AuthResponse>>

    @POST("api/v1/auth/logout")
    suspend fun logout(): Response<ApiResponse<Unit>>

    @POST("api/v1/auth/google")
    suspend fun googleLogin(
        @Body request: GoogleLoginRequest
    ): Response<ApiResponse<AuthResponse>>
}