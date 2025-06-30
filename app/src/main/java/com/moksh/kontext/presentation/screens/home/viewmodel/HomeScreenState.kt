package com.moksh.kontext.presentation.screens.home.viewmodel

import com.moksh.kontext.domain.model.ProjectDto

data class HomeScreenState(
    val isLoading: Boolean = false,
    val projects: List<ProjectDto> = emptyList(),
    val errorMessage: String? = null,
    val isDeleting: Boolean = false,
    val deletingProjectId: String? = null
)

data class CreateProjectBottomSheetState(
    val isVisible: Boolean = false,
    val isLoading: Boolean = false,
    val projectName: String = "",
    val projectDescription: String = "",
    val errorMessage: String? = null
)