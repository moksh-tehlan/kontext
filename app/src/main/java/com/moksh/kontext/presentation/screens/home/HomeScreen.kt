package com.moksh.kontext.presentation.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.moksh.kontext.presentation.core.theme.KontextTheme
import com.moksh.kontext.presentation.core.utils.ObserveAsEvents
import com.moksh.kontext.presentation.screens.home.components.CreateProjectBottomSheet
import com.moksh.kontext.presentation.screens.home.components.HomeEmptyState
import com.moksh.kontext.presentation.screens.home.viewmodel.CreateProjectBottomSheetState
import com.moksh.kontext.presentation.screens.home.viewmodel.HomeScreenActions
import com.moksh.kontext.presentation.screens.home.viewmodel.HomeScreenEvents
import com.moksh.kontext.presentation.screens.home.viewmodel.HomeScreenState
import com.moksh.kontext.presentation.screens.home.viewmodel.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )


    ObserveAsEvents(viewModel.homeEvents) { event ->
        when (event) {
            is HomeScreenEvents.ShowError -> {
                scope.launch { snackbarHostState.showSnackbar(event.message) }
            }

            is HomeScreenEvents.ProjectCreatedSuccessfully -> {
                scope.launch { snackbarHostState.showSnackbar("Project created successfully!") }
            }

            is HomeScreenEvents.ProjectDeletedSuccessfully -> {
                scope.launch { snackbarHostState.showSnackbar("Project deleted") }
            }

            is HomeScreenEvents.CloseCreateProjectBottomSheet -> {
                scope.launch {
                    bottomSheetState.hide()
                }
            }
        }
    }

    HomeScreenView(
        state = viewModel.homeState.collectAsState().value,
        createBottomSheetState = viewModel.bottomSheetState.collectAsState().value,
        snackbarHostState = snackbarHostState,
        bottomSheetState = bottomSheetState,
        scope = scope,
        action = viewModel::onAction
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenView(
    state: HomeScreenState,
    createBottomSheetState: CreateProjectBottomSheetState,
    snackbarHostState: SnackbarHostState,
    bottomSheetState: SheetState,
    scope: CoroutineScope,
    action: (HomeScreenActions) -> Unit,
) {

    Scaffold(
        contentColor = MaterialTheme.colorScheme.surface,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    action(HomeScreenActions.ShowCreateProjectBottomSheet)
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Create Project"
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            HomeEmptyState()
        }
    }

    if (createBottomSheetState.isVisible) {
        CreateProjectBottomSheet(
            onDismiss = {
                scope.launch {
                    bottomSheetState.hide().apply {
                        action(HomeScreenActions.HideCreateProjectBottomSheet)
                    }
                }
            },
            sheetState = bottomSheetState,
            isLoading = createBottomSheetState.isLoading,
            name = createBottomSheetState.projectName,
            description = createBottomSheetState.projectDescription,
            onNameChange = { action(HomeScreenActions.ProjectNameChange(it)) },
            onDescriptionChange = { action(HomeScreenActions.ProjectDescriptionChange(it)) },
            onCreateProject = { action(HomeScreenActions.CreateNewProject) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    KontextTheme {
        HomeScreenView(
            state = HomeScreenState(),
            createBottomSheetState = CreateProjectBottomSheetState(),
            snackbarHostState = remember { SnackbarHostState() },
            bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            scope = rememberCoroutineScope(),
            action = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenDarkPreview() {
    KontextTheme(darkTheme = true) {
        HomeScreenView(
            state = HomeScreenState(),
            createBottomSheetState = CreateProjectBottomSheetState(),
            snackbarHostState = remember { SnackbarHostState() },
            bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            scope = rememberCoroutineScope(),
            action = {}
        )
    }
}