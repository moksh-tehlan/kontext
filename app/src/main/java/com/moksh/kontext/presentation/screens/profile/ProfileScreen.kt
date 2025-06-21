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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.moksh.kontext.presentation.common.KontextButton
import com.moksh.kontext.presentation.common.KontextTextField
import com.moksh.kontext.presentation.common.backArrowIcon
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

@Composable
fun ProfileScreenContent(
    state: ProfileState,
    onAction: (ProfileActions) -> Unit,
    snackbarHostState: SnackbarHostState
) {

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = backArrowIcon,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onAction(ProfileActions.NavigateBack) }
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Profile",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(36.dp))
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
                    .clickable { onAction(ProfileActions.DeleteAccount) },
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