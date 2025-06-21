package com.moksh.kontext.presentation.screens.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {

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
            _state.value = _state.value.copy(isLoading = true)
            try {
                // TODO: Load user profile from repository
                // For now, setting default values
                _state.value = _state.value.copy(
                    fullName = "Moksh Tehlan",
                    nickname = "Moksh",
                    isLoading = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = "Failed to load profile: ${e.message}"
                )
                _events.emit(ProfileEvents.ShowError("Failed to load profile"))
            }
        }
    }

    private fun updateProfile() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isUpdating = true, errorMessage = null)
            try {
                // TODO: Update user profile via repository
                // Simulate API call
                kotlinx.coroutines.delay(1000)

                _state.value = _state.value.copy(isUpdating = false)
                _events.emit(ProfileEvents.ShowSuccess("Profile updated successfully"))
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isUpdating = false,
                    errorMessage = "Failed to update profile: ${e.message}"
                )
                _events.emit(ProfileEvents.ShowError("Failed to update profile"))
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
            try {
                // TODO: Delete user account via repository
                // Remove all user data, sessions, etc.
                kotlinx.coroutines.delay(1500) // Simulate API call

                _state.value = _state.value.copy(isDeleting = false)
                _events.emit(ProfileEvents.NavigateToAuth)
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isDeleting = false,
                    errorMessage = "Failed to delete account: ${e.message}"
                )
                _events.emit(ProfileEvents.ShowError("Failed to delete account"))
            }
        }
    }
}