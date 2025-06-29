package com.moksh.kontext.domain.repository

import com.moksh.kontext.data.model.auth.AuthResponse
import com.moksh.kontext.domain.utils.DataError
import com.moksh.kontext.domain.utils.EmptyResult
import com.moksh.kontext.domain.utils.Result

interface AuthRepository {

    suspend fun sendOtp(email: String): EmptyResult<DataError>

    suspend fun login(email: String, otp: String): Result<AuthResponse, DataError>

    suspend fun refreshToken(): Result<AuthResponse, DataError>

    suspend fun logout(): EmptyResult<DataError>

    fun isLoggedIn(): Boolean

    fun clearSession()
}