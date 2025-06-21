package com.moksh.kontext.presentation.screens.profile.viewmodel

data class ProfileState(
    val fullName: String = "",
    val nickname: String = "",
    val isLoading: Boolean = false,
    val isUpdating: Boolean = false,
    val isDeleting: Boolean = false,
    val errorMessage: String? = null,
    val isUpdateEnabled: Boolean = false
)