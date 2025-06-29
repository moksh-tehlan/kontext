package com.moksh.kontext.domain.model

data class UpdateUserDto(
    val nickname: String? = null,
    val email: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val profilePictureUrl: String? = null,
    val role: String? = null
)