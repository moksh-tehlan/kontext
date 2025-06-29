package com.moksh.kontext.presentation.screens.profile.viewmodel

data class ProfileState(
    val userId: String = "",
    val fullName: String = "",
    val nickname: String = "",
    val email: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val isLoading: Boolean = false,
    val isUpdating: Boolean = false,
    val isDeleting: Boolean = false,
    val showDeleteAccountDialog: Boolean = false,
    val errorMessage: String? = null,
    val isUpdateEnabled: Boolean = false
)