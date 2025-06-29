package com.moksh.kontext.data.mapper

import com.moksh.kontext.data.model.auth.AuthResponse
import com.moksh.kontext.domain.model.AuthDto

fun AuthResponse.toDto(): AuthDto {
    return AuthDto(
        accessToken = accessToken,
        refreshToken = refreshToken,
        tokenType = tokenType,
        expiresIn = expiresIn,
        user = user.toDto()
    )
}

fun AuthDto.toModel(): AuthResponse {
    return AuthResponse(
        accessToken = accessToken,
        refreshToken = refreshToken,
        tokenType = tokenType,
        expiresIn = expiresIn,
        user = user.toModel()
    )
}