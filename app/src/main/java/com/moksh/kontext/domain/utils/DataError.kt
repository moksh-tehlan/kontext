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
}