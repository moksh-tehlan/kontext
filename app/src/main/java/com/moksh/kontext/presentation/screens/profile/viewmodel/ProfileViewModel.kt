package com.moksh.kontext.presentation.screens.profile.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moksh.kontext.domain.model.UpdateUserDto
import com.moksh.kontext.domain.repository.UserRepository
import com.moksh.kontext.domain.utils.DataError
import com.moksh.kontext.domain.utils.Result
import com.moksh.kontext.presentation.core.utils.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state = combine(
        _state,
        _state
    ) { state, _ ->
        state.copy(
            isUpdateEnabled = state.fullName.isNotBlank() &&
                    state.nickname.isNotBlank() &&
                    !state.isUpdating
        )
    }.stateIn(
        scope = viewModelScope,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000L),
        initialValue = ProfileState()
    )

    private val _events = MutableSharedFlow<ProfileEvents>()
    val events = _events.asSharedFlow()

    init {
        loadUserProfile()
    }

    fun onAction(action: ProfileActions) {
        when (action) {
            is ProfileActions.UpdateFullName -> {
                _state.value = _state.value.copy(
                    fullName = action.fullName,
                    errorMessage = null
                )
            }

            is ProfileActions.UpdateNickname -> {
                _state.value = _state.value.copy(
                    nickname = action.nickname,
                    errorMessage = null
                )
            }

            is ProfileActions.UpdateProfile -> {
                updateProfile()
            }

            is ProfileActions.ShowDeleteAccountDialog -> {
                _state.value = _state.value.copy(showDeleteAccountDialog = true)
            }

            is ProfileActions.ConfirmDeleteAccount -> {
                deleteAccount()
            }

            is ProfileActions.DismissDeleteAccountDialog -> {
                _state.value = _state.value.copy(showDeleteAccountDialog = false)
            }

            is ProfileActions.NavigateBack -> {
                viewModelScope.launch {
                    _events.emit(ProfileEvents.NavigateBack)
                }
            }

            is ProfileActions.ClearError -> {
                _state.value = _state.value.copy(errorMessage = null)
            }
        }
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            // First try to load from cache
            val cachedUser = userRepository.getCachedUser()
            if (cachedUser != null) {
                _state.value = _state.value.copy(
                    userId = cachedUser.id,
                    fullName = cachedUser.fullName,
                    nickname = cachedUser.nickname,
                    email = cachedUser.email,
                    firstName = cachedUser.firstName,
                    lastName = cachedUser.lastName,
                    isLoading = false
                )
                return@launch
            }

            // If no cached user, fetch from network
            _state.value = _state.value.copy(isLoading = true)
            when (val result = userRepository.getCurrentUser()) {
                is Result.Success -> {
                    val user = result.data
                    _state.value = _state.value.copy(
                        userId = user.id,
                        fullName = user.fullName,
                        nickname = user.nickname,
                        email = user.email,
                        firstName = user.firstName,
                        lastName = user.lastName,
                        isLoading = false
                    )
                }

                is Result.Error -> {
                    if (result.error == DataError.Network.UNAUTHORIZED) {
                        // Token refresh failed, redirect to auth
                        _events.emit(ProfileEvents.NavigateToAuth)
                    } else {
                        val errorMessage = result.error.asUiText().asString(context)
                        _state.value = _state.value.copy(
                            isLoading = false,
                            errorMessage = errorMessage
                        )
                        _events.emit(ProfileEvents.ShowError(errorMessage))
                    }
                }
            }
        }
    }

    private fun updateProfile() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isUpdating = true, errorMessage = null)

            val currentState = _state.value
            val nameParts = currentState.fullName.trim().split(" ", limit = 2)
            val firstName = nameParts.firstOrNull()?.trim() ?: ""
            val lastName = if (nameParts.size > 1) nameParts[1].trim() else ""

            val updateUserDto = UpdateUserDto(
                nickname = currentState.nickname.trim().takeIf { it.isNotBlank() },
                firstName = firstName.takeIf { it.isNotBlank() },
                lastName = lastName.takeIf { it.isNotBlank() }
            )

            when (val result = userRepository.updateUser(currentState.userId, updateUserDto)) {
                is Result.Success -> {
                    val updatedUser = result.data
                    _state.value = _state.value.copy(
                        fullName = updatedUser.fullName,
                        nickname = updatedUser.nickname,
                        firstName = updatedUser.firstName,
                        lastName = updatedUser.lastName,
                        isUpdating = false
                    )
                    _events.emit(ProfileEvents.ShowSuccess("Profile updated successfully"))
                }

                is Result.Error -> {
                    if (result.error == DataError.Network.UNAUTHORIZED) {
                        // Token refresh failed, redirect to auth
                        _events.emit(ProfileEvents.NavigateToAuth)
                    } else {
                        val errorMessage = result.error.asUiText().asString(context)
                        _state.value = _state.value.copy(
                            isUpdating = false,
                            errorMessage = errorMessage
                        )
                        _events.emit(ProfileEvents.ShowError(errorMessage))
                    }
                }
            }
        }
    }

    private fun deleteAccount() {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isDeleting = true,
                errorMessage = null,
                showDeleteAccountDialog = false
            )

            when (val result = userRepository.deleteCurrentUser()) {
                is Result.Success -> {
                    _state.value = _state.value.copy(isDeleting = false)
                    _events.emit(ProfileEvents.NavigateToAuth)
                }

                is Result.Error -> {
                    if (result.error == DataError.Network.UNAUTHORIZED) {
                        // Token refresh failed, redirect to auth
                        _events.emit(ProfileEvents.NavigateToAuth)
                    } else {
                        val errorMessage = result.error.asUiText().asString(context)
                        _state.value = _state.value.copy(
                            isDeleting = false,
                            errorMessage = errorMessage
                        )
                        _events.emit(ProfileEvents.ShowError(errorMessage))
                    }
                }
            }
        }
    }
}