package com.moksh.kontext.data.repository

import com.moksh.kontext.data.api.UserApiService
import com.moksh.kontext.data.model.user.User
import com.moksh.kontext.data.utils.safeCall
import com.moksh.kontext.domain.repository.UserRepository
import com.moksh.kontext.domain.utils.DataError
import com.moksh.kontext.domain.utils.Result
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userApiService: UserApiService
) : UserRepository {

    override suspend fun getCurrentUser(): Result<User, DataError> {
        return when (val result = safeCall { userApiService.getCurrentUser() }) {
            is Result.Success -> {
                result.data.data?.let { user ->
                    Result.Success(user)
                } ?: Result.Error(DataError.Network.EMPTY_RESPONSE)
            }

            is Result.Error -> result
        }
    }
}