package com.moksh.kontext.domain.utils

sealed interface DataError : Error {
    enum class Network : DataError {
        REQUEST_TIMEOUT,
        UNAUTHORIZED,
        CONFLICT,
        TOO_MANY_REQUESTS,
        NO_INTERNET,
        PAYLOAD_TOO_LARGE,
        SERVER_ERROR,
        SERIALIZATION,
        EMPTY_RESPONSE,
        UNKNOWN
    }

    enum class Local : DataError {
        DISK_FULL,
        UNKNOWN,
        DUPLICATE_DATA,
        DATABASE_FULL,
        SQL_ERROR
    }

    enum class Auth : DataError {
        OTP_MISMATCH,
        OTP_EXPIRED,
        OTP_NOT_FOUND,
        ACCOUNT_DEACTIVATED,
        USER_CREATION_FAILED,
        INVALID_REFRESH_TOKEN,
        GOOGLE_TOKEN_INVALID,
        AUTHENTICATION_FAILED,
        AUTHORIZATION_FAILED,
        JWT_TOKEN_EXPIRED,
        JWT_TOKEN_MALFORMED,
        JWT_TOKEN_MISSING,
        JWT_SIGNATURE_INVALID,
        ACCESS_TOKEN_INVALID,
        TOKEN_BLACKLISTED
    }

    enum class User : DataError {
        DUPLICATE_EMAIL,
        PROFILE_NOT_FOUND,
        PROFILE_UPDATE_FAILED,
        INVALID_PROFILE_DATA,
        INSUFFICIENT_PERMISSIONS
    }

    enum class Project : DataError {
        PROJECT_NOT_FOUND,
        PROJECT_CREATE_FAILED,
        PROJECT_UPDATE_FAILED,
        PROJECT_DELETE_FAILED
    }
}