package com.moksh.kontext.data.repository

import com.moksh.kontext.data.api.AuthApiService
import com.moksh.kontext.data.local.TokenManager
import com.moksh.kontext.data.model.auth.AuthResponse
import com.moksh.kontext.data.model.auth.LoginRequest
import com.moksh.kontext.data.model.auth.RefreshTokenRequest
import com.moksh.kontext.data.model.auth.SendOtpRequest
import com.moksh.kontext.data.utils.safeCall
import com.moksh.kontext.domain.repository.AuthRepository
import com.moksh.kontext.domain.utils.DataError
import com.moksh.kontext.domain.utils.EmptyResult
import com.moksh.kontext.domain.utils.Result
import com.moksh.kontext.domain.utils.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authApiService: AuthApiService,
    private val tokenManager: TokenManager
) : AuthRepository {

    override suspend fun sendOtp(email: String): EmptyResult<DataError> {
        return safeCall {
            authApiService.sendOtp(SendOtpRequest(email))
        }.map { Unit }
    }

    override suspend fun login(email: String, otp: String): Result<AuthResponse, DataError> {
        return when (val result = safeCall { authApiService.login(LoginRequest(email, otp)) }) {
            is Result.Success -> {
                result.data.data?.let { authResponse ->
                    tokenManager.saveTokens(
                        accessToken = authResponse.accessToken,
                        refreshToken = authResponse.refreshToken
                    )
                    Result.Success(authResponse)
                } ?: Result.Error(DataError.Network.EMPTY_RESPONSE)
            }

            is Result.Error -> result
        }
    }

    override suspend fun refreshToken(): Result<AuthResponse, DataError> {
        val refreshToken = tokenManager.getRefreshToken()
            ?: return Result.Error(DataError.Network.UNAUTHORIZED)

        return when (val result =
            safeCall { authApiService.refreshToken(RefreshTokenRequest(refreshToken)) }) {
            is Result.Success -> {
                result.data.data?.let { authResponse ->
                    tokenManager.saveTokens(
                        accessToken = authResponse.accessToken,
                        refreshToken = authResponse.refreshToken
                    )
                    Result.Success(authResponse)
                } ?: Result.Error(DataError.Network.EMPTY_RESPONSE)
            }

            is Result.Error -> result
        }
    }

    override suspend fun logout(): EmptyResult<DataError> {
        return safeCall {
            authApiService.logout()
        }.map {
            tokenManager.clearTokens()
        }
    }

    override fun isLoggedIn(): Boolean {
        return tokenManager.hasValidTokens()
    }

    override fun clearSession() {
        tokenManager.clearTokens()
    }
}