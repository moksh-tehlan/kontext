package com.moksh.kontext.presentation.core.utils


import com.moksh.kontext.R
import com.moksh.kontext.domain.utils.DataError

fun DataError.asUiText(): UiText {
    return when (this) {
        DataError.Network.SERVER_ERROR -> UiText.StringResource(
            R.string.server_error,
        )

        else -> UiText.StringResource(R.string.unknown_error)
    }
}