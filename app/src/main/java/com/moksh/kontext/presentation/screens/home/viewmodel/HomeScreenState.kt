package com.moksh.kontext.presentation.screens.home.viewmodel

data class HomeScreenState(
    val isLoading: Boolean = false
)

data class CreateProjectBottomSheetState(
    val isVisible: Boolean = false,
    val isLoading: Boolean = false,
    val projectName: String = "",
    val projectDescription: String = "",
    val errorMessage: String? = null
)