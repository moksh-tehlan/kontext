package com.moksh.kontext.domain.model

data class UserDto(
    val id: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val nickname: String,
    val authProvider: String,
    val isEmailVerified: Boolean,
    val profilePictureUrl: String?,
    val role: String,
    val isActive: Boolean
) {
    val fullName: String
        get() = "$firstName $lastName".trim()
}