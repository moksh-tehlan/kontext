package com.moksh.kontext.presentation.screens.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow().stateIn(
        scope = viewModelScope,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000L),
        initialValue = SettingsState()
    )

    private val _events = MutableSharedFlow<SettingsEvents>()
    val events = _events.asSharedFlow()

    init {
        loadUserSettings()
    }

    fun onAction(action: SettingsActions) {
        when (action) {
            is SettingsActions.NavigateBack -> {
                viewModelScope.launch {
                    _events.emit(SettingsEvents.NavigateBack)
                }
            }

            is SettingsActions.ShowInfo -> {
                viewModelScope.launch {
                    _events.emit(SettingsEvents.ShowInfo("App Version: 1.0.0\nBuild: 001"))
                }
            }

            is SettingsActions.NavigateToProfile -> {
                viewModelScope.launch {
                    _events.emit(SettingsEvents.NavigateToProfile)
                }
            }

            is SettingsActions.NavigateToBilling -> {
                viewModelScope.launch {
                    _events.emit(SettingsEvents.NavigateToBilling)
                }
            }

            is SettingsActions.NavigateToUpgrade -> {
                viewModelScope.launch {
                    _events.emit(SettingsEvents.NavigateToUpgrade)
                }
            }

            is SettingsActions.ToggleHapticFeedback -> {
                toggleHapticFeedback()
            }

            is SettingsActions.Logout -> {
                logout()
            }

            is SettingsActions.ClearError -> {
                _state.value = _state.value.copy(errorMessage = null)
            }
        }
    }

    private fun loadUserSettings() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                // TODO: Load user settings from repository
                // For now, setting default values
                _state.value = _state.value.copy(
                    userEmail = "someone@gmail.com",
                    userTier = "Free",
                    isHapticFeedbackEnabled = true,
                    isLoading = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = "Failed to load settings: ${e.message}"
                )
                _events.emit(SettingsEvents.ShowError("Failed to load settings"))
            }
        }
    }

    private fun toggleHapticFeedback() {
        viewModelScope.launch {
            try {
                val newValue = !_state.value.isHapticFeedbackEnabled
                // TODO: Save haptic feedback preference via repository

                _state.value = _state.value.copy(
                    isHapticFeedbackEnabled = newValue
                )

                val message =
                    if (newValue) "Haptic feedback enabled" else "Haptic feedback disabled"
                _events.emit(SettingsEvents.ShowSuccess(message))
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    errorMessage = "Failed to update haptic feedback: ${e.message}"
                )
                _events.emit(SettingsEvents.ShowError("Failed to update setting"))
            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoggingOut = true, errorMessage = null)
            try {
                // TODO: Logout user via repository
                // Clear user session, tokens, etc.
                kotlinx.coroutines.delay(1000) // Simulate API call

                _state.value = _state.value.copy(isLoggingOut = false)
                _events.emit(SettingsEvents.NavigateToAuth)
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoggingOut = false,
                    errorMessage = "Failed to logout: ${e.message}"
                )
                _events.emit(SettingsEvents.ShowError("Failed to logout"))
            }
        }
    }
}