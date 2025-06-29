package com.moksh.kontext.data.network

import com.moksh.kontext.data.local.TokenManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val accessToken = tokenManager.getAccessToken()

        val newRequest =
            if (!accessToken.isNullOrEmpty() && shouldAddAuthHeader(originalRequest.url.encodedPath)) {
                originalRequest.newBuilder()
                    .addHeader("Authorization", "Bearer $accessToken")
                    .build()
            } else {
                originalRequest
            }

        return chain.proceed(newRequest)
    }

    private fun shouldAddAuthHeader(path: String): Boolean {
        val publicEndpoints = listOf(
            "/api/v1/auth/login",
            "/api/v1/auth/send-otp"
        )
        return !publicEndpoints.any { path.contains(it) }
    }
}