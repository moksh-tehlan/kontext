package com.moksh.kontext.data.repository

import com.moksh.kontext.data.api.UserApiService
import com.moksh.kontext.data.local.UserPreferences
import com.moksh.kontext.data.mapper.toDto
import com.moksh.kontext.data.mapper.toRequest
import com.moksh.kontext.data.utils.safeCall
import com.moksh.kontext.domain.model.UpdateUserDto
import com.moksh.kontext.domain.model.UserDto
import com.moksh.kontext.domain.repository.UserRepository
import com.moksh.kontext.domain.utils.DataError
import com.moksh.kontext.domain.utils.Result
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userApiService: UserApiService,
    private val userPreferences: UserPreferences
) : UserRepository {

    override suspend fun getCurrentUser(): Result<UserDto, DataError> {
        return when (val result = safeCall { userApiService.getCurrentUser() }) {
            is Result.Success -> {
                result.data.data?.let { user ->
                    val userDto = user.toDto()
                    cacheUser(userDto)
                    Result.Success(userDto)
                } ?: Result.Error(DataError.Network.EMPTY_RESPONSE)
            }

            is Result.Error -> result
        }
    }

    override suspend fun updateUser(
        userId: String,
        updateUserDto: UpdateUserDto
    ): Result<UserDto, DataError> {
        return when (val result = safeCall {
            userApiService.updateUser(userId, updateUserDto.toRequest())
        }) {
            is Result.Success -> {
                result.data.data?.let { user ->
                    val userDto = user.toDto()
                    cacheUser(userDto)
                    Result.Success(userDto)
                } ?: Result.Error(DataError.Network.EMPTY_RESPONSE)
            }

            is Result.Error -> result
        }
    }

    override suspend fun deleteCurrentUser(): Result<Unit, DataError> {
        return when (val result = safeCall { userApiService.deleteCurrentUser() }) {
            is Result.Success -> {
                clearCachedUser()
                Result.Success(Unit)
            }
            is Result.Error -> result
        }
    }

    override suspend fun getCachedUser(): UserDto? {
        return userPreferences.getUser()
    }

    override suspend fun cacheUser(user: UserDto) {
        userPreferences.saveUser(user)
    }

    override suspend fun clearCachedUser() {
        userPreferences.clearUser()
    }
}