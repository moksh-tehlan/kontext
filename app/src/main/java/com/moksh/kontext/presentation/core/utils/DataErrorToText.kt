package com.moksh.kontext.presentation.core.utils


import com.moksh.kontext.R
import com.moksh.kontext.domain.utils.DataError

fun DataError.asUiText(): UiText {
    return when (this) {
        DataError.Network.REQUEST_TIMEOUT -> UiText.StringResource(R.string.error_request_timeout)
        DataError.Network.UNAUTHORIZED -> UiText.StringResource(R.string.error_unauthorized)
        DataError.Network.CONFLICT -> UiText.StringResource(R.string.error_conflict)
        DataError.Network.TOO_MANY_REQUESTS -> UiText.StringResource(R.string.error_too_many_requests)
        DataError.Network.NO_INTERNET -> UiText.StringResource(R.string.error_no_internet)
        DataError.Network.PAYLOAD_TOO_LARGE -> UiText.StringResource(R.string.error_payload_too_large)
        DataError.Network.SERVER_ERROR -> UiText.StringResource(R.string.server_error)
        DataError.Network.SERIALIZATION -> UiText.StringResource(R.string.error_serialization)
        DataError.Network.EMPTY_RESPONSE -> UiText.StringResource(R.string.error_empty_response)
        DataError.Network.UNKNOWN -> UiText.StringResource(R.string.unknown_error)

        DataError.Local.DISK_FULL -> UiText.StringResource(R.string.error_disk_full)
        DataError.Local.DUPLICATE_DATA -> UiText.StringResource(R.string.error_duplicate_data)
        DataError.Local.DATABASE_FULL -> UiText.StringResource(R.string.error_database_full)
        DataError.Local.SQL_ERROR -> UiText.StringResource(R.string.error_sql)
        DataError.Local.UNKNOWN -> UiText.StringResource(R.string.unknown_error)

        DataError.Auth.OTP_MISMATCH -> UiText.StringResource(R.string.error_otp_mismatch)
        DataError.Auth.OTP_EXPIRED -> UiText.StringResource(R.string.error_otp_expired)
        DataError.Auth.OTP_NOT_FOUND -> UiText.StringResource(R.string.error_otp_not_found)
        DataError.Auth.ACCOUNT_DEACTIVATED -> UiText.StringResource(R.string.error_account_deactivated)
        DataError.Auth.USER_CREATION_FAILED -> UiText.StringResource(R.string.error_user_creation_failed)
        DataError.Auth.INVALID_REFRESH_TOKEN -> UiText.StringResource(R.string.error_invalid_refresh_token)
        DataError.Auth.GOOGLE_TOKEN_INVALID -> UiText.StringResource(R.string.error_google_token_invalid)
        DataError.Auth.AUTHENTICATION_FAILED -> UiText.StringResource(R.string.error_authentication_failed)
        DataError.Auth.AUTHORIZATION_FAILED -> UiText.StringResource(R.string.error_authorization_failed)
    }
}