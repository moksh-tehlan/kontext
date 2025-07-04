package com.moksh.kontext.presentation.navigation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.moksh.kontext.presentation.screens.auth.AuthScreen
import com.moksh.kontext.presentation.screens.chat.ChatScreen
import com.moksh.kontext.presentation.screens.home.HomeScreen
import com.moksh.kontext.presentation.screens.knowledge_source.KnowledgeSourceScreen
import com.moksh.kontext.presentation.screens.otp.OtpScreen
import com.moksh.kontext.presentation.screens.profile.ProfileScreen
import com.moksh.kontext.presentation.screens.project.ProjectScreen
import com.moksh.kontext.presentation.screens.settings.SettingsScreen

@Composable
fun KontextNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier.background(color = MaterialTheme.colorScheme.surface),
    startDestination: Routes = Graphs.HomeGraph
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
        enterTransition = {
            slideInHorizontally(
                animationSpec = tween(
                    durationMillis = 500,
                    easing = FastOutSlowInEasing
                ),
                initialOffsetX = { fullWidth -> fullWidth }
            )
        },
        exitTransition = {
            slideOutHorizontally(
                animationSpec = tween(
                    durationMillis = 500,
                    easing = FastOutSlowInEasing
                ),
                targetOffsetX = { fullWidth -> -fullWidth }
            ) + fadeOut(animationSpec = tween(500))
        },
        popEnterTransition = {
            slideInHorizontally(
                animationSpec = tween(
                    durationMillis = 500,
                    easing = FastOutSlowInEasing
                ),
                initialOffsetX = { fullWidth -> -fullWidth }
            ) + fadeIn(animationSpec = tween(500))
        },
        popExitTransition = {
            slideOutHorizontally(
                animationSpec = tween(
                    durationMillis = 500,
                    easing = FastOutSlowInEasing
                ),
                targetOffsetX = { fullWidth -> fullWidth }
            )
        }
    ) {
        navigation<Graphs.AuthGraph>(
            startDestination = AuthRoutes.AuthScreen
        ) {
            composable<AuthRoutes.AuthScreen> {
                AuthScreen(
                    onNavigateToOtp = {email ->
                        navController.navigate(AuthRoutes.OtpScreen(email = email))
                    },
                    onNavigateToHome = {
                        navController.navigate(Graphs.HomeGraph) {
                            popUpTo(Graphs.AuthGraph) {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            composable<AuthRoutes.OtpScreen> {
                OtpScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onNavigateToHome = {
                        navController.navigate(Graphs.HomeGraph) {
                            popUpTo(Graphs.AuthGraph) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
        }

        navigation<Graphs.HomeGraph>(
            startDestination = HomeRoutes.HomeScreen
        ) {
            composable<HomeRoutes.HomeScreen> {
                HomeScreen(
                    navigateToProject = { projectId ->
                        navController.navigate(HomeRoutes.ProjectScreen(projectId = projectId)) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    navigateToSettings = {
                        navController.navigate(HomeRoutes.SettingsScreen) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
            composable<HomeRoutes.ProjectScreen> {
                ProjectScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onNavigateToChat = { projectId, chatId ->
                        navController.navigate(
                            HomeRoutes.ChatScreen(
                                projectId = projectId,
                                chatId = chatId
                            )
                        ) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onNavigateToKnowledgeSource = { projectId ->
                        navController.navigate(
                            HomeRoutes.KnowledgeSourceScreen(projectId = projectId)
                        ) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }

            composable<HomeRoutes.KnowledgeSourceScreen> {
                KnowledgeSourceScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable<HomeRoutes.ChatScreen> {
                ChatScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable<HomeRoutes.ProfileScreen> {
                ProfileScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onNavigateToAuth = {
                        navController.navigate(Graphs.AuthGraph) {
                            popUpTo(Graphs.HomeGraph) {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            composable<HomeRoutes.SettingsScreen> {
                SettingsScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onNavigateToProfile = {
                        navController.navigate(HomeRoutes.ProfileScreen) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onNavigateToBilling = {
                        // TODO: Navigate to billing screen when implemented
                    },
                    onNavigateToUpgrade = {
                        // TODO: Navigate to upgrade screen when implemented
                    },
                    onNavigateToAuth = {
                        navController.navigate(Graphs.AuthGraph) {
                            popUpTo(Graphs.HomeGraph) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
        }
    }
}