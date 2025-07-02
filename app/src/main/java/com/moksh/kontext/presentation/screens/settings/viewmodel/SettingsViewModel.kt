package com.moksh.kontext.presentation.screens.settings.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moksh.kontext.BuildConfig
import com.moksh.kontext.domain.repository.AuthRepository
import com.moksh.kontext.domain.repository.UserRepository
import com.moksh.kontext.domain.utils.DataError
import com.moksh.kontext.domain.utils.Result
import com.moksh.kontext.presentation.core.utils.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

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
                _state.value = _state.value.copy(showInfoDropdown = true)
            }

            is SettingsActions.DismissInfoDropdown -> {
                _state.value = _state.value.copy(showInfoDropdown = false)
            }

            is SettingsActions.OnConsumerTermsClick -> {
                viewModelScope.launch {
                    _events.emit(SettingsEvents.OpenExternalLink(BuildConfig.TERMS_URL))
                }
            }

            is SettingsActions.OnPrivacyPolicyClick -> {
                viewModelScope.launch {
                    _events.emit(SettingsEvents.OpenExternalLink(BuildConfig.PRIVACY_URL))
                }
            }

            is SettingsActions.OnHelpSupportClick -> {
                viewModelScope.launch {
                    _events.emit(SettingsEvents.OpenExternalLink(BuildConfig.CONTACT_URL))
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

            is SettingsActions.ShowLogoutDialog -> {
                _state.value = _state.value.copy(showLogoutDialog = true)
            }

            is SettingsActions.ConfirmLogout -> {
                logout()
            }

            is SettingsActions.DismissLogoutDialog -> {
                _state.value = _state.value.copy(showLogoutDialog = false)
            }

            is SettingsActions.ClearError -> {
                _state.value = _state.value.copy(errorMessage = null)
            }
        }
    }

    private fun loadUserSettings() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            when (val result = userRepository.getCurrentUser()) {
                is Result.Success -> {
                    val user = result.data
                    _state.value = _state.value.copy(
                        userEmail = user.email,
                        userTier = "Free", // TODO: Get actual tier from user data
                        isHapticFeedbackEnabled = true, // TODO: Load from preferences
                        isLoading = false
                    )
                }

                is Result.Error -> {
                    if (result.error == DataError.Network.UNAUTHORIZED) {
                        // Token refresh failed, redirect to auth
                        _events.emit(SettingsEvents.NavigateToAuth)
                    } else {
                        val errorMessage = result.error.asUiText().asString(context)
                        _state.value = _state.value.copy(
                            isLoading = false,
                            errorMessage = errorMessage
                        )
                        _events.emit(SettingsEvents.ShowError(errorMessage))
                    }
                }
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
            _state.value = _state.value.copy(
                isLoggingOut = true,
                errorMessage = null,
                showLogoutDialog = false
            )

            when (val result = authRepository.logout()) {
                is Result.Success -> {
                    _state.value = _state.value.copy(isLoggingOut = false)
                    _events.emit(SettingsEvents.NavigateToAuth)
                }

                is Result.Error -> {
                    val errorMessage = result.error.asUiText().asString(context)
                    _state.value = _state.value.copy(
                        isLoggingOut = false,
                        errorMessage = errorMessage
                    )
                    _events.emit(SettingsEvents.ShowError(errorMessage))
                }
            }
        }
    }

}