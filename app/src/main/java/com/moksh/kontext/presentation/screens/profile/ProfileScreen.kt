package com.moksh.kontext.presentation.screens.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.moksh.kontext.presentation.common.ConfirmationDialog
import com.moksh.kontext.presentation.common.KontextButton
import com.moksh.kontext.presentation.common.KontextTextField
import com.moksh.kontext.presentation.common.backArrowIcon
import com.moksh.kontext.presentation.common.deleteIcon
import com.moksh.kontext.presentation.core.theme.AuthTextSecondary
import com.moksh.kontext.presentation.core.theme.KontextTheme
import com.moksh.kontext.presentation.core.utils.ObserveAsEvents
import com.moksh.kontext.presentation.screens.profile.viewmodel.ProfileActions
import com.moksh.kontext.presentation.screens.profile.viewmodel.ProfileEvents
import com.moksh.kontext.presentation.screens.profile.viewmodel.ProfileState
import com.moksh.kontext.presentation.screens.profile.viewmodel.ProfileViewModel
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit,
    onNavigateToAuth: () -> Unit = {},
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    ObserveAsEvents(flow = viewModel.events) { event ->
        when (event) {
            is ProfileEvents.NavigateBack -> onNavigateBack()
            is ProfileEvents.NavigateToAuth -> onNavigateToAuth()
            is ProfileEvents.ShowError -> {
                scope.launch { snackbarHostState.showSnackbar(event.message) }
            }

            is ProfileEvents.ShowSuccess -> {
                scope.launch { snackbarHostState.showSnackbar(event.message) }
            }
        }
    }

    ProfileScreenContent(
        state = state,
        onAction = viewModel::onAction,
        snackbarHostState = snackbarHostState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreenContent(
    state: ProfileState,
    onAction: (ProfileActions) -> Unit,
    snackbarHostState: SnackbarHostState
) {

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Profile",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onAction(ProfileActions.NavigateBack) }) {
                        Icon(
                            modifier = Modifier.size(28.dp),
                            imageVector = backArrowIcon,
                            contentDescription = "Navigation back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            // Full Name
            Text(
                text = "Full name",
                style = MaterialTheme.typography.bodyLarge,
                color = AuthTextSecondary,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            KontextTextField(
                value = state.fullName,
                onValueChange = { onAction(ProfileActions.UpdateFullName(it)) },
                placeholder = "Full name",
                enabled = !state.isLoading && !state.isUpdating
            )
            Spacer(modifier = Modifier.height(24.dp))
            // Nickname
            Text(
                text = "What should we call you?",
                style = MaterialTheme.typography.bodyLarge,
                color = AuthTextSecondary,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            KontextTextField(
                value = state.nickname,
                onValueChange = { onAction(ProfileActions.UpdateNickname(it)) },
                placeholder = "Nickname",
                enabled = !state.isLoading && !state.isUpdating
            )
            Spacer(modifier = Modifier.height(32.dp))
            KontextButton(
                text = if (state.isUpdating) "Updating..." else "Update Profile",
                onClick = { onAction(ProfileActions.UpdateProfile) },
                enabled = state.isUpdateEnabled && !state.isUpdating
            )
            Spacer(modifier = Modifier.height(40.dp))
            // Account Actions
            Text(
                text = "Account Actions",
                style = MaterialTheme.typography.bodyLarge,
                color = AuthTextSecondary,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onAction(ProfileActions.ShowDeleteAccountDialog) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Account",
                    tint = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (state.isDeleting) "Deleting..." else "Delete Account",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // Delete Account Confirmation Dialog
        ConfirmationDialog(
            isVisible = state.showDeleteAccountDialog,
            title = "Delete Account",
            message = "Are you sure you want to delete your account? This action cannot be undone and all your data will be permanently removed.",
            confirmText = "Delete Account",
            cancelText = "Cancel",
            icon = deleteIcon,
            isDestructive = true,
            isLoading = state.isDeleting,
            onConfirm = { onAction(ProfileActions.ConfirmDeleteAccount) },
            onCancel = { onAction(ProfileActions.DismissDeleteAccountDialog) }
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    KontextTheme {
        ProfileScreenContent(
            state = ProfileState(
                fullName = "Moksh Tehlan",
                nickname = "Moksh",
                isUpdateEnabled = true
            ),
            onAction = { },
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenLoadingPreview() {
    KontextTheme {
        ProfileScreenContent(
            state = ProfileState(
                fullName = "Moksh Tehlan",
                nickname = "Moksh",
                isLoading = true
            ),
            onAction = { },
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}