package com.moksh.kontext.presentation.screens.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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
class HomeViewModel @Inject constructor() : ViewModel() {

    private val _homeState = MutableStateFlow(HomeScreenState())
    val homeState = _homeState.asStateFlow()
        .onStart { }
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
        }
    }

    private fun createProject() {
        viewModelScope.launch {
            try {
                _bottomSheetState.update {
                    it.copy(
                        isLoading = true,
                        errorMessage = null
                    )
                }
                delay(2000L)
                _homeEvents.emit(HomeScreenEvents.ProjectCreatedSuccessfully)
                _homeEvents.emit(HomeScreenEvents.CloseCreateProjectBottomSheet)
            } catch (e: Exception) {
                _homeState.value = _homeState.value.copy(isLoading = false)
                _homeEvents.emit(HomeScreenEvents.ShowError("Failed to create project: ${e.message}"))
            }
        }
    }

    private fun deleteProject(projectId: String) {
        viewModelScope.launch {

        }
    }
}
