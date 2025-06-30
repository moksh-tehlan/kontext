package com.moksh.kontext.domain.utils

import com.moksh.kontext.domain.model.UserDto
import com.moksh.kontext.domain.repository.UserRepository

suspend fun UserRepository.getCurrentUserWithCache(): UserDto? {
    // First try to get from cache
    getCachedUser()?.let { return it }

    // If not in cache, try to fetch from network
    return when (val result = getCurrentUser()) {
        is Result.Success -> result.data
        is Result.Error -> null
    }
}