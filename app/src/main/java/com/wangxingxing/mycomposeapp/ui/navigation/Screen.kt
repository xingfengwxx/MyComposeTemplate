package com.wangxingxing.mycomposeapp.ui.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object Profile : Screen("profile")
    object Settings : Screen("settings")
    object Detail : Screen("detail/{userId}") {
        fun createRoute(userId: Int) = "detail/$userId"
    }
}

