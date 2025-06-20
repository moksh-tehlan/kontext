package com.moksh.kontext.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.moksh.kontext.presentation.screens.auth.AuthScreen
import com.moksh.kontext.presentation.screens.home.HomeScreen
import com.moksh.kontext.presentation.screens.profile.ProfileScreen
import com.moksh.kontext.presentation.screens.settings.SettingsScreen

@Composable
fun KontextNavGraph(
    navController: NavHostController,
    modifier: androidx.compose.ui.Modifier = androidx.compose.ui.Modifier,
    startDestination: String = Graphs.AuthGraph
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        navigation(
            startDestination = Screen.Auth,
            route = Graphs.AuthGraph
        ) {
            composable(Screen.Auth) {
                AuthScreen()
            }
        }
        
        navigation(
            startDestination = Screen.Home,
            route = Graphs.HomeGraph
        ) {
            composable(Screen.Home) {
                HomeScreen(
                    onNavigateToProfile = {
                        navController.navigate(Screen.Profile)
                    },
                    onNavigateToSettings = {
                        navController.navigate(Screen.Settings)
                    }
                )
            }
            
            composable(Screen.Profile) {
                ProfileScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
            
            composable(Screen.Settings) {
                SettingsScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}