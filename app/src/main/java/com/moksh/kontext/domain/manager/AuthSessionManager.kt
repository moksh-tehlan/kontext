package com.moksh.kontext.domain.manager

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthSessionManager @Inject constructor() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private val _authExpiredEvents = MutableSharedFlow<Unit>()
    val authExpiredEvents = _authExpiredEvents.asSharedFlow()

    fun notifyAuthExpired() {
        scope.launch {
            _authExpiredEvents.emit(Unit)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: AuthSessionManager? = null

        fun getInstance(): AuthSessionManager? = INSTANCE

        fun setInstance(instance: AuthSessionManager) {
            INSTANCE = instance
        }
    }
}