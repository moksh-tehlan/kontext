package com.moksh.kontext.data.network

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.moksh.kontext.data.local.TokenManager
import com.moksh.kontext.data.model.ApiResponse
import com.moksh.kontext.data.model.auth.AuthResponse
import com.moksh.kontext.data.model.auth.RefreshTokenRequest
import com.moksh.kontext.domain.manager.AuthSessionManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Named

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager,
    @Named("refresh_client") private val refreshClient: OkHttpClient,
    private val gson: Gson,
    private val baseUrl: String
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Add auth header if needed
        val requestWithAuth = addAuthHeader(originalRequest)

        // Execute request
        var response = chain.proceed(requestWithAuth)

        // Check if we got 401/JWT error and should refresh
        if (shouldRefreshToken(response) && !isRefreshEndpoint(originalRequest.url.encodedPath)) {
            Log.d("AuthInterceptor", "Token expired, attempting refresh")

            synchronized(this) {
                // Double-check token is still expired (another thread might have refreshed)
                val currentToken = tokenManager.getAccessToken()
                if (currentToken == tokenManager.getAccessToken()) {

                    val refreshResult = runBlocking {
                        refreshToken()
                    }

                    if (refreshResult) {
                        // Token refreshed successfully, retry original request
                        Log.d("AuthInterceptor", "Token refreshed, retrying request")
                        response.close() // Close the original response

                        val newRequestWithFreshToken = addAuthHeader(originalRequest)
                        response = chain.proceed(newRequestWithFreshToken)
                    } else {
                        // Refresh failed, notify for logout
                        Log.d("AuthInterceptor", "Token refresh failed, notifying logout")
                        AuthSessionManager.getInstance()?.notifyAuthExpired()
                    }
                }
            }
        }

        return response
    }

    private fun addAuthHeader(originalRequest: okhttp3.Request): okhttp3.Request {
        val accessToken = tokenManager.getAccessToken()
        return if (!accessToken.isNullOrEmpty() && shouldAddAuthHeader(originalRequest.url.encodedPath)) {
            originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer $accessToken")
                .build()
        } else {
            originalRequest
        }
    }
    
    private fun shouldAddAuthHeader(path: String): Boolean {
        val publicEndpoints = listOf(
            "/api/v1/auth/login",
            "/api/v1/auth/send-otp",
            "/api/v1/auth/refresh"
        )
        return !publicEndpoints.any { path.contains(it) }
    }

    private fun shouldRefreshToken(response: Response): Boolean {
        return response.code == 401 || isJwtError(response.code)
    }

    private fun isJwtError(statusCode: Int): Boolean {
        return statusCode in 4303..4308 // JWT specific error codes
    }

    private fun isRefreshEndpoint(path: String): Boolean {
        return path.contains("/api/v1/auth/refresh")
    }

    private suspend fun refreshToken(): Boolean {
        return try {
            val refreshToken = tokenManager.getRefreshToken()
            if (refreshToken != null) {

                val requestBody = gson.toJson(RefreshTokenRequest(refreshToken))
                    .toRequestBody("application/json".toMediaType())

                val request = Request.Builder()
                    .url("${baseUrl}api/v1/auth/refresh")
                    .post(requestBody)
                    .build()

                val response = refreshClient.newCall(request).execute()

                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val apiResponse = gson.fromJson(
                        responseBody,
                        object : TypeToken<ApiResponse<AuthResponse>>() {}.type
                    ) as ApiResponse<AuthResponse>

                    apiResponse.data?.let { authResponse ->
                        tokenManager.saveTokens(
                            accessToken = authResponse.accessToken,
                            refreshToken = authResponse.refreshToken
                        )
                        true
                    } ?: false
                } else {
                    tokenManager.clearTokens()
                    false
                }
            } else {
                false
            }
        } catch (e: Exception) {
            Log.e("AuthInterceptor", "Token refresh failed", e)
            tokenManager.clearTokens()
            false
        }
    }
}