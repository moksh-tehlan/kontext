package com.moksh.kontext.presentation.screens.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AuthScreenViewModel @Inject constructor() : ViewModel() {
    private val _authState = MutableStateFlow(AuthScreenState())
    val authState = _authState.asStateFlow()
        .onStart { }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            AuthScreenState()
        )

    fun onAction(action: AuthScreenActions) {
        when (action) {
            // Handle actions here
        }
    }

    private val _authEvents = MutableSharedFlow<AuthScreenEvents>()
    val authEvents = _authEvents.asSharedFlow()
}