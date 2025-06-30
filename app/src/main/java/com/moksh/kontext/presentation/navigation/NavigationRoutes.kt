package com.moksh.kontext.presentation.navigation

import kotlinx.serialization.Serializable

interface Routes

sealed interface Graphs : Routes {
    @Serializable
    data object AuthGraph : Graphs

    @Serializable
    data object HomeGraph : Graphs
}

sealed interface AuthRoutes : Routes {

    @Serializable
    data object AuthScreen : AuthRoutes

    @Serializable
    data class OtpScreen(val email: String) :
        AuthRoutes
}

sealed interface HomeRoutes : Routes {

    @Serializable
    data object HomeScreen : HomeRoutes

    @Serializable
    data class ProjectScreen(val projectId: String) : HomeRoutes

    @Serializable
    data class KnowledgeSourceScreen(val projectId: String) : HomeRoutes

    @Serializable
    data object ProfileScreen : HomeRoutes

    @Serializable
    data object SettingsScreen : HomeRoutes

    @Serializable
    data class ChatScreen(val projectId: String, val chatId: String) : HomeRoutes
}