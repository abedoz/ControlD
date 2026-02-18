package com.controld.app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector? = null) {
    object Login : Screen("login", "Login")
    object Dashboard : Screen("dashboard", "Dashboard", Icons.Default.Home)
    object Devices : Screen("devices", "Devices", Icons.Default.Devices)
    object Profiles : Screen("profiles", "Profiles", Icons.Default.Shield)
    object ProfileDetail : Screen("profile_detail/{profileId}/{profileName}", "Profile Detail") {
        fun createRoute(profileId: String, profileName: String) = "profile_detail/$profileId/$profileName"
    }
    object Analytics : Screen("analytics", "Analytics", Icons.Default.Analytics)

    companion object {
        val bottomNavItems = listOf(Dashboard, Devices, Profiles, Analytics)
    }
}
