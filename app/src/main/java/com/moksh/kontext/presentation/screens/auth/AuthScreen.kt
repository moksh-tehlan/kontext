package com.moksh.kontext.presentation.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.moksh.kontext.presentation.core.theme.AuthBackgroundDark
import com.moksh.kontext.presentation.core.theme.KontextTheme
import com.moksh.kontext.presentation.core.utils.ObserveAsEvents
import com.moksh.kontext.presentation.screens.auth.components.AuthFooter
import com.moksh.kontext.presentation.screens.auth.components.AuthForm
import com.moksh.kontext.presentation.screens.auth.components.AuthHeader
import com.moksh.kontext.presentation.screens.auth.viewmodel.AuthScreenActions
import com.moksh.kontext.presentation.screens.auth.viewmodel.AuthScreenEvents
import com.moksh.kontext.presentation.screens.auth.viewmodel.AuthScreenState
import com.moksh.kontext.presentation.screens.auth.viewmodel.AuthScreenViewModel

@Composable
fun AuthScreen(
    onNavigateToHome: () -> Unit = {},
    onNavigateToOtp: (String) -> Unit = {},
    viewModel: AuthScreenViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    
    ObserveAsEvents(viewModel.authEvents) { event ->
        when (event) {
            is AuthScreenEvents.ShowError -> {
                // Handle error display in the UI layer if needed
            }
            is AuthScreenEvents.NavigateToHome -> {
                onNavigateToHome()
            }
            is AuthScreenEvents.NavigateToOtp -> {
                onNavigateToOtp(event.email)
            }
        }
    }
    
    AuthScreenView(
        state = viewModel.authState.collectAsState().value,
        action = viewModel::onAction,
        snackbarHostState = snackbarHostState
    )
}

@Composable
private fun AuthScreenView(
    state: AuthScreenState,
    action: (AuthScreenActions) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    // Handle error display
    state.emailError?.let { error ->
        LaunchedEffect(error) {
            snackbarHostState.showSnackbar(error)
        }
    }
    
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AuthBackgroundDark)
                .padding(paddingValues)
                .padding(horizontal = 24.dp,)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                AuthHeader()
                
                Spacer(modifier = Modifier.height(12.dp))
                
                AuthForm(
                    email = state.email,
                    onEmailChange = { action(AuthScreenActions.OnEmailChanged(it)) },
                    onContinueWithEmail = { action(AuthScreenActions.OnContinueWithEmail) },
                    onContinueWithGoogle = { action(AuthScreenActions.OnContinueWithGoogle) },
                    isEmailLoading = state.isEmailLoading,
                    isGoogleLoading = state.isGoogleLoading
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                AuthFooter(
                    modifier = Modifier.fillMaxSize(),
                    onTermsClick = { action(AuthScreenActions.OnTermsClick) },
                    onUsagePolicyClick = { action(AuthScreenActions.OnUsagePolicyClick) },
                    onPrivacyPolicyClick = { action(AuthScreenActions.OnPrivacyPolicyClick) }
                )
                
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1A1A1A)
@Composable
private fun AuthScreenPreview() {
    KontextTheme(darkTheme = true) {
        AuthScreenView(
            state = AuthScreenState(
                email = "user@example.com",
                isEmailLoading = false,
                isGoogleLoading = false,
                emailError = null
            ),
            action = {},
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1A1A1A)
@Composable
private fun AuthScreenLoadingPreview() {
    KontextTheme(darkTheme = true) {
        AuthScreenView(
            state = AuthScreenState(
                email = "user@example.com",
                isEmailLoading = true,
                isGoogleLoading = false,
                emailError = null
            ),
            action = {},
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}