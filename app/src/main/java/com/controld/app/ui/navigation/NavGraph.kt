package com.controld.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.controld.app.ui.analytics.AnalyticsScreen
import com.controld.app.ui.dashboard.DashboardScreen
import com.controld.app.ui.devices.DevicesScreen
import com.controld.app.ui.login.LoginScreen
import com.controld.app.ui.profiles.ProfileDetailScreen
import com.controld.app.ui.profiles.ProfilesScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String,
    onLogout: () -> Unit
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Dashboard.route) {
            DashboardScreen(onLogout = onLogout)
        }

        composable(Screen.Devices.route) {
            DevicesScreen()
        }

        composable(Screen.Profiles.route) {
            ProfilesScreen(
                onProfileClick = { profileId, profileName ->
                    navController.navigate(Screen.ProfileDetail.createRoute(profileId, profileName))
                }
            )
        }

        composable(
            route = Screen.ProfileDetail.route,
            arguments = listOf(
                navArgument("profileId") { type = NavType.StringType },
                navArgument("profileName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val profileId = backStackEntry.arguments?.getString("profileId") ?: ""
            val profileName = backStackEntry.arguments?.getString("profileName") ?: ""
            ProfileDetailScreen(
                profileId = profileId,
                profileName = profileName,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Analytics.route) {
            AnalyticsScreen()
        }
    }
}
