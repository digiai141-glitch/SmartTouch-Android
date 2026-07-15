package com.smarttouch.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.smarttouch.app.presentation.ui.screens.AboutScreen
import com.smarttouch.app.presentation.ui.screens.GestureConfigScreen
import com.smarttouch.app.presentation.ui.screens.HomeScreen
import com.smarttouch.app.presentation.ui.screens.SettingsScreen

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object GestureConfig : Screen("gesture_config")
    data object Settings : Screen("settings")
    data object About : Screen("about")
}

@Composable
fun SmartTouchNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToGestures = { navController.navigate(Screen.GestureConfig.route) },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
                onNavigateToAbout = { navController.navigate(Screen.About.route) },
            )
        }
        composable(Screen.GestureConfig.route) {
            GestureConfigScreen(
                onNavigateBack = { navController.popBackStack() },
            )
        }
        composable(Screen.Settings.route) {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() },
            )
        }
        composable(Screen.About.route) {
            AboutScreen(
                onNavigateBack = { navController.popBackStack() },
            )
        }
    }
}
