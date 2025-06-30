package com.moksh.kontext.presentation.screens.home.viewmodel

// Actions are the action performed by the user like button press, project creation
sealed interface HomeScreenActions {
    data object ShowCreateProjectBottomSheet : HomeScreenActions
    data class OnProjectClick(val projectId: String) : HomeScreenActions
    data object HideCreateProjectBottomSheet : HomeScreenActions
    data class ProjectNameChange(val name: String) : HomeScreenActions
    data class ProjectDescriptionChange(val description: String) : HomeScreenActions
    data object CreateNewProject : HomeScreenActions
    data class DeleteProject(val projectId: String) : HomeScreenActions
    data object RefreshProjects : HomeScreenActions
}
