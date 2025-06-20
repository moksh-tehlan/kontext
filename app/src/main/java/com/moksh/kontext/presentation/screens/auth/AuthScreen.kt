package com.moksh.kontext.presentation.screens.auth

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.moksh.kontext.presentation.core.utils.ObserveAsEvents
import com.moksh.kontext.presentation.screens.auth.viewmodel.AuthScreenActions
import com.moksh.kontext.presentation.screens.auth.viewmodel.AuthScreenState
import com.moksh.kontext.presentation.screens.auth.viewmodel.AuthScreenViewModel

@Composable
fun AuthScreen(
    viewModel: AuthScreenViewModel = hiltViewModel()
) {
    ObserveAsEvents(viewModel.authEvents) {event->
        when (event) {
            else -> {}
        }
    }
    AuthScreenView(
        state = viewModel.authState.collectAsState().value,
        action = viewModel::onAction
    )
}

@Composable
private fun AuthScreenView(
    state: AuthScreenState,
    action: (AuthScreenActions) -> Unit,
) {
    Text("Auth Screen")
}