package com.moksh.kontext.presentation.screens.otp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.moksh.kontext.presentation.common.backArrowIcon
import com.moksh.kontext.presentation.core.theme.AuthBackgroundDark
import com.moksh.kontext.presentation.core.theme.KontextTheme
import com.moksh.kontext.presentation.core.utils.ObserveAsEvents
import com.moksh.kontext.presentation.screens.otp.components.OtpForm
import com.moksh.kontext.presentation.screens.otp.viewmodel.OtpScreenActions
import com.moksh.kontext.presentation.screens.otp.viewmodel.OtpScreenEvents
import com.moksh.kontext.presentation.screens.otp.viewmodel.OtpScreenState
import com.moksh.kontext.presentation.screens.otp.viewmodel.OtpScreenViewModel

@Composable
fun OtpScreen(
    onNavigateToHome: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: OtpScreenViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }

    
    ObserveAsEvents(viewModel.otpEvents) { event ->
        when (event) {
            is OtpScreenEvents.ShowError -> {
                // Error is handled in the UI layer via state
            }
            is OtpScreenEvents.NavigateToHome -> {
                onNavigateToHome()
            }
            is OtpScreenEvents.NavigateBack -> {
                onNavigateBack()
            }
        }
    }
    
    OtpScreenView(
        state = viewModel.otpState.collectAsState().value,
        action = viewModel::onAction,
        snackbarHostState = snackbarHostState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OtpScreenView(
    state: OtpScreenState,
    action: (OtpScreenActions) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    
    // Handle success message display
    state.successMessage?.let { message ->
        LaunchedEffect(message) {
            snackbarHostState.showSnackbar(message)
        }
    }
    
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Kontext",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { action(OtpScreenActions.OnBackPressed) }) {
                        Icon(
                            modifier = Modifier.size(28.dp),
                            imageVector = backArrowIcon,
                            contentDescription = "Navigation back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AuthBackgroundDark
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AuthBackgroundDark)
                .padding(paddingValues)
                .padding(horizontal = 36.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                
                OtpForm(
                    otp = state.otp,
                    onOtpChange = { action(OtpScreenActions.OnOtpChanged(it)) },
                    isVerifying = state.isVerifying,
                    error = state.otpError?.asString(),
                    email = state.email,
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1A1A1A)
@Composable
private fun OtpScreenPreview() {
    KontextTheme(darkTheme = true) {
        OtpScreenView(
            state = OtpScreenState(
                email = "someone@gmail.com",
                otp = "123",
                isVerifying = false,
                isResending = false,
                otpError = null,
                resendCooldown = 45,
                showResendButton = false
            ),
            action = {},
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1A1A1A)
@Composable
private fun OtpScreenCompletePreview() {
    KontextTheme(darkTheme = true) {
        OtpScreenView(
            state = OtpScreenState(
                email = "someone@gmail.com",
                otp = "123456",
                isVerifying = false,
                isResending = false,
                otpError = null,
                resendCooldown = 0,
                showResendButton = true
            ),
            action = {},
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1A1A1A)
@Composable
private fun OtpScreenVerifyingPreview() {
    KontextTheme(darkTheme = true) {
        OtpScreenView(
            state = OtpScreenState(
                email = "someone@gmail.com",
                otp = "123456",
                isVerifying = true,
                isResending = false,
                otpError = null,
                resendCooldown = 0,
                showResendButton = true
            ),
            action = {},
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}
