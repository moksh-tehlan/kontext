package com.moksh.kontext.domain.repository

import com.moksh.kontext.domain.model.UserDto
import com.moksh.kontext.domain.utils.DataError
import com.moksh.kontext.domain.utils.Result

interface UserRepository {

    suspend fun getCurrentUser(): Result<UserDto, DataError>
}