package com.moksh.kontext.domain.repository

import com.moksh.kontext.data.model.user.User
import com.moksh.kontext.domain.utils.DataError
import com.moksh.kontext.domain.utils.Result

interface UserRepository {

    suspend fun getCurrentUser(): Result<User, DataError>
}