package com.moksh.kontext.presentation.navigation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.moksh.kontext.presentation.screens.auth.AuthScreen
import com.moksh.kontext.presentation.screens.home.HomeScreen
import com.moksh.kontext.presentation.screens.otp.OtpScreen
import com.moksh.kontext.presentation.screens.profile.ProfileScreen
import com.moksh.kontext.presentation.screens.settings.SettingsScreen

@Composable
fun KontextNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: Routes = Graphs.AuthGraph
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
                    onNavigateToProfile = {},
                    onNavigateToSettings = {}
                )
            }

            composable<AuthRoutes.OtpScreen> {
                OtpScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onNavigateToHome = {}
                )
            }
        }
    }
}