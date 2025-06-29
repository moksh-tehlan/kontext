package com.moksh.kontext.data.utils

import android.util.Log
import com.google.gson.JsonSyntaxException
import com.moksh.kontext.data.model.ApiResponse
import com.moksh.kontext.data.model.ErrorResponse
import com.moksh.kontext.domain.repository.AuthRepository
import com.moksh.kontext.domain.utils.DataError
import com.moksh.kontext.domain.utils.Result
import retrofit2.Response
import java.net.SocketTimeoutException

typealias GenericResponse<T> = ApiResponse<T>

suspend inline fun <T> safeCall(
    authRepository: AuthRepository? = null,
    call: () -> Response<ApiResponse<T>>
): Result<ApiResponse<T>, DataError> {
    return try {
        val response = call()
        val result = responseToResult(response)

        // Check if it's a JWT error that requires token refresh and authRepository is provided
        if (result is Result.Error && authRepository != null && shouldRefreshToken(result.error)) {
            Log.d("SafeCall", "JWT error detected, attempting token refresh")

            when (authRepository.refreshToken()) {
                is Result.Success -> {
                    Log.d("SafeCall", "Token refreshed successfully, retrying original call")
                    // Token refreshed successfully, retry the original call
                    val retryResponse = call()
                    responseToResult(retryResponse)
                }

                is Result.Error -> {
                    Log.d("SafeCall", "Token refresh failed, clearing session")
                    // Token refresh failed, clear session and return unauthorized
                    authRepository.clearSession()
                    Result.Error(DataError.Network.UNAUTHORIZED)
                }
            }
        } else {
            result
        }
    } catch (e: Exception) {
        when (e) {
            is SocketTimeoutException -> Result.Error(DataError.Network.REQUEST_TIMEOUT)
            is JsonSyntaxException -> Result.Error(DataError.Network.SERVER_ERROR)
            else -> Result.Error(DataError.Network.UNKNOWN)
        }.also { e.printStackTrace() }
    }
}

fun shouldRefreshToken(error: DataError): Boolean {
    return when (error) {
        DataError.Auth.JWT_TOKEN_EXPIRED,
        DataError.Auth.JWT_TOKEN_MALFORMED,
        DataError.Auth.JWT_TOKEN_MISSING,
        DataError.Auth.JWT_SIGNATURE_INVALID,
        DataError.Auth.ACCESS_TOKEN_INVALID -> true

        DataError.Auth.TOKEN_BLACKLISTED,
        DataError.Auth.INVALID_REFRESH_TOKEN -> false // Don't retry for these
        else -> false
    }
}

fun <T> responseToResult(response: Response<ApiResponse<T>>): Result<ApiResponse<T>, DataError> {
    Log.d("SafeCall", "Response: ${response.code()} - ${response.message()}")

    if (response.isSuccessful) {
        val body = response.body()
        return if (body != null) {
            if (body.success) {
                Result.Success(body)
            } else {
                Log.d("SafeCall", "API returned success=false: ${body.message}")
                mapApiErrorToDataError(body.status)
            }
        } else {
            Result.Error(DataError.Network.EMPTY_RESPONSE)
        }
    }

    val errorBody = response.errorBody()
    val errorBodyString = errorBody?.string()
    Log.d("SafeCall", "Error body: $errorBodyString")

    val errorResponse = if (!errorBodyString.isNullOrEmpty()) {
        try {
            JsonConverter.fromJson<ErrorResponse>(errorBodyString)
        } catch (e: Exception) {
            Log.e("SafeCall", "Failed to parse error response", e)
            null
        }
    } else null

    val statusCode = errorResponse?.status ?: response.code()
    return mapApiErrorToDataError(statusCode)
}

private fun mapApiErrorToDataError(statusCode: Int): Result.Error<DataError> {
    return Result.Error(
        when (statusCode) {
            // OTP Related Errors (4101-4199)
            4101 -> DataError.Auth.OTP_MISMATCH
            4102 -> DataError.Auth.OTP_EXPIRED
            4103 -> DataError.Auth.OTP_NOT_FOUND

            // Account Related Errors (4200-4299)
            4201 -> DataError.Auth.ACCOUNT_DEACTIVATED
            4202 -> DataError.Auth.USER_CREATION_FAILED
            
            // Token Related Errors (4300-4399)
            4301 -> DataError.Auth.INVALID_REFRESH_TOKEN
            4302 -> DataError.Auth.GOOGLE_TOKEN_INVALID
            4303 -> DataError.Auth.JWT_TOKEN_EXPIRED
            4304 -> DataError.Auth.JWT_TOKEN_MALFORMED
            4305 -> DataError.Auth.JWT_TOKEN_MISSING
            4306 -> DataError.Auth.JWT_SIGNATURE_INVALID
            4307 -> DataError.Auth.ACCESS_TOKEN_INVALID
            4308 -> DataError.Auth.TOKEN_BLACKLISTED

            // General Auth Errors (4000-4099)
            4001 -> DataError.Auth.AUTHENTICATION_FAILED
            4002 -> DataError.Auth.AUTHORIZATION_FAILED

            // User Profile Errors (5000-5199)
            5001 -> DataError.User.PROFILE_NOT_FOUND
            5002 -> DataError.User.PROFILE_UPDATE_FAILED
            5003 -> DataError.User.INVALID_PROFILE_DATA
            5101 -> DataError.User.INSUFFICIENT_PERMISSIONS
            5202 -> DataError.User.DUPLICATE_EMAIL

            // Fallback for auth error range
            in 4000..4399 -> DataError.Auth.AUTHENTICATION_FAILED

            // Standard HTTP error codes
            401 -> DataError.Network.UNAUTHORIZED
            408 -> DataError.Network.REQUEST_TIMEOUT
            409 -> DataError.Network.CONFLICT
            413 -> DataError.Network.PAYLOAD_TOO_LARGE
            429 -> DataError.Network.TOO_MANY_REQUESTS
            in 500..599 -> DataError.Network.SERVER_ERROR
            else -> DataError.Network.UNKNOWN
        }
    )
}