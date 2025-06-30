package com.moksh.kontext.presentation.screens.home.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moksh.kontext.domain.model.CreateProjectDto
import com.moksh.kontext.domain.repository.ProjectRepository
import com.moksh.kontext.domain.utils.Result
import com.moksh.kontext.presentation.core.utils.asUiText
import com.moksh.kontext.presentation.screens.home.viewmodel.HomeScreenEvents.NavigateToProject
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val projectRepository: ProjectRepository
) : ViewModel() {

    private val _homeState = MutableStateFlow(HomeScreenState())
    val homeState = _homeState.asStateFlow()
        .onStart {
            loadProjects()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            HomeScreenState()
        )

    private val _bottomSheetState = MutableStateFlow(CreateProjectBottomSheetState())
    val bottomSheetState = _bottomSheetState.asStateFlow()
        .onStart { }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            CreateProjectBottomSheetState()
        )

    private val _homeEvents = MutableSharedFlow<HomeScreenEvents>()
    val homeEvents = _homeEvents.asSharedFlow()

    fun onAction(action: HomeScreenActions) {
        when (action) {
            is HomeScreenActions.ShowCreateProjectBottomSheet -> {
                _bottomSheetState.update {
                    it.copy(
                        isVisible = true,
                        isLoading = false,
                        projectName = "",
                        projectDescription = "",
                        errorMessage = null
                    )
                }
            }

            is HomeScreenActions.HideCreateProjectBottomSheet -> {
                _bottomSheetState.update {
                    it.copy(
                        isVisible = false,
                        isLoading = false,
                        projectName = "",
                        projectDescription = "",
                        errorMessage = null
                    )
                }
            }

            is HomeScreenActions.DeleteProject -> {
                deleteProject(action.projectId)
            }

            HomeScreenActions.CreateNewProject -> {
                createProject()
            }

            is HomeScreenActions.ProjectDescriptionChange -> {
                _bottomSheetState.update {
                    it.copy(
                        projectDescription = action.description,
                        errorMessage = null
                    )
                }
            }

            is HomeScreenActions.ProjectNameChange -> {
                _bottomSheetState.update {
                    it.copy(
                        projectName = action.name,
                        errorMessage = null
                    )
                }
            }

            is HomeScreenActions.OnProjectClick -> {
                viewModelScope.launch { _homeEvents.emit(NavigateToProject(action.projectId)) }
            }

            HomeScreenActions.RefreshProjects -> {
                loadProjects()
            }
        }
    }

    private fun loadProjects() {
        viewModelScope.launch {
            _homeState.update { it.copy(isLoading = true, errorMessage = null) }

            when (val result = projectRepository.getAllProjects()) {
                is Result.Success -> {
                    _homeState.update {
                        it.copy(
                            isLoading = false,
                            projects = result.data,
                            errorMessage = null
                        )
                    }
                }

                is Result.Error -> {

                    val errorMessage = result.error.asUiText().asString(context)
                    _homeState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = errorMessage
                        )
                    }
                    _homeEvents.emit(HomeScreenEvents.ShowError(errorMessage))
                }
            }
        }
    }

    private fun createProject() {
        viewModelScope.launch {
            val currentBottomSheetState = _bottomSheetState.value

            if (currentBottomSheetState.projectName.isBlank()) {
                _bottomSheetState.update {
                    it.copy(errorMessage = "Project name is required")
                }
                return@launch
            }

            _bottomSheetState.update {
                it.copy(isLoading = true, errorMessage = null)
            }

            val createProjectDto = CreateProjectDto(
                name = currentBottomSheetState.projectName.trim(),
                description = currentBottomSheetState.projectDescription.trim()
                    .takeIf { it.isNotBlank() }
            )

            when (val result = projectRepository.createProject(createProjectDto)) {
                is Result.Success -> {
                    _bottomSheetState.update {
                        it.copy(
                            isLoading = false,
                            isVisible = false,
                            projectName = "",
                            projectDescription = "",
                            errorMessage = null
                        )
                    }
                    _homeEvents.emit(HomeScreenEvents.ProjectCreatedSuccessfully)
                    loadProjects() // Refresh the projects list
                }

                is Result.Error -> {

                    val errorMessage = result.error.asUiText().asString(context)
                    _bottomSheetState.update {
                        it.copy(isLoading = false, errorMessage = errorMessage)
                    }
                }
            }
        }
    }

    private fun deleteProject(projectId: String) {
        viewModelScope.launch {
            _homeState.update {
                it.copy(
                    isDeleting = true,
                    deletingProjectId = projectId
                )
            }

            when (val result = projectRepository.deleteProject(projectId)) {
                is Result.Success -> {
                    _homeState.update {
                        it.copy(
                            isDeleting = false,
                            deletingProjectId = null,
                            projects = it.projects.filter { project -> project.id != projectId }
                        )
                    }
//                    _homeEvents.emit(HomeScreenEvents.ShowSuccess("Project deleted successfully"))
                }

                is Result.Error -> {
                    _homeState.update {
                        it.copy(
                            isDeleting = false,
                            deletingProjectId = null
                        )
                    }


                    val errorMessage = result.error.asUiText().asString(context)
                    _homeEvents.emit(HomeScreenEvents.ShowError(errorMessage))
                }
            }
        }
    }
}
