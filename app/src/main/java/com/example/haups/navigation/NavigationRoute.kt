package com.example.haups.navigation

sealed class NavigationRoute(val route: String) {
    data object Home : NavigationRoute("home")
    data object Schedule : NavigationRoute("schedule")
    data object Logs : NavigationRoute("logs")
    data object Settings : NavigationRoute("settings")
}
