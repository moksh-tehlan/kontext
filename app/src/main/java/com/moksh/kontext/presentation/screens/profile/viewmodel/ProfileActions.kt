package com.moksh.kontext.presentation.screens.profile.viewmodel

/**
 * Actions that represent user interactions with the Profile screen
 */
sealed interface ProfileActions {
    data class UpdateFullName(val fullName: String) : ProfileActions
    data class UpdateNickname(val nickname: String) : ProfileActions
    data object UpdateProfile : ProfileActions
    data object ShowDeleteAccountDialog : ProfileActions
    data object ConfirmDeleteAccount : ProfileActions
    data object DismissDeleteAccountDialog : ProfileActions
    data object NavigateBack : ProfileActions
    data object ClearError : ProfileActions
}