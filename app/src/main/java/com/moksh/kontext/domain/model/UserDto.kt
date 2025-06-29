package com.moksh.kontext.domain.model

data class UserDto(
    val id: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val authProvider: String,
    val isEmailVerified: Boolean,
    val role: String,
    val isActive: Boolean
) {
    val fullName: String
        get() = "$firstName $lastName".trim()

    val displayName: String
        get() = if (fullName.isNotBlank()) fullName else email
}