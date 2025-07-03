package com.moksh.kontext.presentation.screens.settings

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.moksh.kontext.BuildConfig
import com.moksh.kontext.presentation.common.ConfirmationDialog
import com.moksh.kontext.presentation.common.backArrowIcon
// import com.moksh.kontext.presentation.common.billingIcon
import com.moksh.kontext.presentation.common.hapticFeedback
import com.moksh.kontext.presentation.common.infoIcon
import com.moksh.kontext.presentation.common.logoutIcon
import com.moksh.kontext.presentation.common.projectIcon
import com.moksh.kontext.presentation.core.theme.KontextTheme
import com.moksh.kontext.presentation.core.utils.ObserveAsEvents
import com.moksh.kontext.presentation.screens.settings.components.AccountTier
import com.moksh.kontext.presentation.screens.settings.components.InfoDropdownMenu
import com.moksh.kontext.presentation.screens.settings.components.SettingsItem
import com.moksh.kontext.presentation.screens.settings.components.ToggleSettingItem
// import com.moksh.kontext.presentation.screens.settings.components.UpgradeCard
import com.moksh.kontext.presentation.screens.settings.viewmodel.SettingsActions
import com.moksh.kontext.presentation.screens.settings.viewmodel.SettingsEvents
import com.moksh.kontext.presentation.screens.settings.viewmodel.SettingsState
import com.moksh.kontext.presentation.screens.settings.viewmodel.SettingsViewModel
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToBilling: () -> Unit = {},
    onNavigateToUpgrade: () -> Unit = {},
    onNavigateToAuth: () -> Unit = {},
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    ObserveAsEvents(flow = viewModel.events) { event ->
        when (event) {
            is SettingsEvents.NavigateBack -> onNavigateBack()
            is SettingsEvents.NavigateToProfile -> onNavigateToProfile()
//            is SettingsEvents.NavigateToBilling -> onNavigateToBilling()
//            is SettingsEvents.NavigateToUpgrade -> onNavigateToUpgrade()
            is SettingsEvents.NavigateToAuth -> onNavigateToAuth()
            is SettingsEvents.ShowError -> {
                scope.launch { snackbarHostState.showSnackbar(event.message) }
            }

            is SettingsEvents.ShowSuccess -> {
                scope.launch { snackbarHostState.showSnackbar(event.message) }
            }

            is SettingsEvents.ShowInfo -> {
                scope.launch { snackbarHostState.showSnackbar(event.message) }
            }

            is SettingsEvents.OpenExternalLink -> {
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(event.url))
                    context.startActivity(intent)
                } catch (e: Exception) {
                    scope.launch { snackbarHostState.showSnackbar("Failed to open link") }
                }
            }
        }
    }

    SettingsScreenContent(
        state = state,
        onAction = viewModel::onAction,
        snackbarHostState = snackbarHostState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenContent(
    state: SettingsState,
    onAction: (SettingsActions) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Settings",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onAction(SettingsActions.NavigateBack) }) {
                        Icon(
                            modifier = Modifier.size(28.dp),
                            imageVector = backArrowIcon,
                            contentDescription = "Navigation back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    Box {
                        IconButton(
                            onClick = { onAction(SettingsActions.ShowInfo) }
                        ) {
                            Icon(
                                modifier = Modifier.size(28.dp),
                                imageVector = infoIcon,
                                contentDescription = "Info menu",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        InfoDropdownMenu(
                            expanded = state.showInfoDropdown,
                            onDismiss = { onAction(SettingsActions.DismissInfoDropdown) },
                            onConsumerTermsClick = { onAction(SettingsActions.OnConsumerTermsClick) },
                            onPrivacyPolicyClick = { onAction(SettingsActions.OnPrivacyPolicyClick) },
                            onHelpSupportClick = { onAction(SettingsActions.OnHelpSupportClick) },
                            appVersion = BuildConfig.VERSION_NAME
                        )
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 24.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            AccountTier(
                email = state.userEmail,
                tier = "", // state.userTier,
            )
            /*
            UpgradeCard(
                onUpgradeClick = { onAction(SettingsActions.NavigateToUpgrade) }
            )
            */
            SettingsItem(
                title = "Profile",
                icon = {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = projectIcon,
                        contentDescription = "profile icon"
                    )
                },
                onClick = { onAction(SettingsActions.NavigateToProfile) }
            )
            /*
            SettingsItem(
                title = "Billing",
                icon = {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = billingIcon,
                        contentDescription = "billing icon"
                    )
                },
                onClick = { onAction(SettingsActions.NavigateToBilling) }
            )
            */
            HorizontalDivider()
            ToggleSettingItem(
                title = "Haptic Feedback",
                isEnabled = state.isHapticFeedbackEnabled,
                icon = {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = hapticFeedback,
                        contentDescription = "haptic feedback icon"
                    )
                },
                onToggle = { onAction(SettingsActions.ToggleHapticFeedback) }
            )
            HorizontalDivider()
            SettingsItem(
                title = if (state.isLoggingOut) "Logging out..." else "Logout",
                textColor = MaterialTheme.colorScheme.error,
                icon = {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.error,
                        imageVector = logoutIcon,
                        contentDescription = "logout icon"
                    )
                },
                onClick = { onAction(SettingsActions.ShowLogoutDialog) }
            )
        }

        // Logout Confirmation Dialog
        ConfirmationDialog(
            isVisible = state.showLogoutDialog,
            title = "Logout",
            message = "Are you sure you want to logout? You'll need to sign in again to access your account.",
            confirmText = "Logout",
            cancelText = "Cancel",
            icon = logoutIcon,
            isDestructive = true,
            isLoading = state.isLoggingOut,
            onConfirm = { onAction(SettingsActions.ConfirmLogout) },
            onCancel = { onAction(SettingsActions.DismissLogoutDialog) }
        )
    }
}

@Composable
@Preview
private fun SettingsScreenPreview() {
    KontextTheme {
        SettingsScreenContent(
            state = SettingsState(
                userEmail = "someone@gmail.com",
                // userTier = "Free",
                isHapticFeedbackEnabled = true
            ),
            onAction = { },
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}

@Composable
@Preview
private fun SettingsScreenLoadingPreview() {
    KontextTheme {
        SettingsScreenContent(
            state = SettingsState(
                userEmail = "someone@gmail.com",
                // userTier = "Premium",
                isHapticFeedbackEnabled = false,
                isLoggingOut = true
            ),
            onAction = { },
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}