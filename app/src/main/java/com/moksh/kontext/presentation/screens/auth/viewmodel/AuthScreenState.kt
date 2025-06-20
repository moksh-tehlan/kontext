package com.moksh.kontext.presentation.screens.auth.viewmodel

data class AuthScreenState(
    val email: String = "",
    val isEmailLoading: Boolean = false,
    val isGoogleLoading: Boolean = false,
    val emailError: String? = null
)