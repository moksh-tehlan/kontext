package com.moksh.kontext.data.mapper

import com.moksh.kontext.data.model.user.User
import com.moksh.kontext.domain.model.UserDto

fun User.toDto(): UserDto {
    return UserDto(
        id = id,
        email = email,
        firstName = firstName,
        lastName = lastName,
        authProvider = authProvider,
        isEmailVerified = isEmailVerified,
        role = role,
        isActive = isActive
    )
}

fun UserDto.toModel(): User {
    return User(
        id = id,
        createdAt = "", // These fields are not exposed in DTO
        updatedAt = "",
        createdBy = "",
        updatedBy = "",
        version = 0,
        isActive = isActive,
        email = email,
        firstName = firstName,
        lastName = lastName,
        authProvider = authProvider,
        isEmailVerified = isEmailVerified,
        role = role
    )
}