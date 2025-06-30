package com.moksh.kontext.presentation.screens.root

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moksh.kontext.domain.manager.AuthSessionManager
import com.moksh.kontext.domain.repository.AuthRepository
import com.moksh.kontext.presentation.navigation.Graphs
import com.moksh.kontext.presentation.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RootViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val authSessionManager: AuthSessionManager
) : ViewModel() {

    private val _startDestination = MutableStateFlow<Routes?>(null)
    val startDestination = _startDestination.asStateFlow()

    private val _shouldNavigateToAuth = MutableStateFlow(false)
    val shouldNavigateToAuth = _shouldNavigateToAuth.asStateFlow()

    init {
        // Set global instance for SafeCall access
        AuthSessionManager.setInstance(authSessionManager)
        checkAuthStatus()
        listenForAuthExpiry()
    }

    private fun checkAuthStatus() {
        viewModelScope.launch {
            val isLoggedIn = authRepository.isLoggedIn()
            _startDestination.value = if (isLoggedIn) {
                Graphs.HomeGraph
            } else {
                Graphs.AuthGraph
            }
        }
    }

    private fun listenForAuthExpiry() {
        viewModelScope.launch {
            authSessionManager.authExpiredEvents.collect {
                _shouldNavigateToAuth.value = true
            }
        }
    }
}