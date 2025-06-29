package com.moksh.kontext.domain.model

data class AuthDto(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String,
    val expiresIn: Int,
    val user: UserDto
)