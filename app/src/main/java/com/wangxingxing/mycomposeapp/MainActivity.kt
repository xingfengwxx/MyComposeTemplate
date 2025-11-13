package com.wangxingxing.mycomposeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.wangxingxing.mycomposeapp.ui.navigation.Screen
import com.wangxingxing.mycomposeapp.ui.screens.DetailScreen
import com.wangxingxing.mycomposeapp.ui.screens.LoginScreen
import com.wangxingxing.mycomposeapp.ui.screens.MainScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Login.route
                    ) {
                        composable(Screen.Login.route) {
                            LoginScreen(
                                onLoginSuccess = {
                                    navController.navigate(Screen.Home.route) {
                                        popUpTo(Screen.Login.route) {
                                            inclusive = true
                                        }
                                    }
                                }
                            )
                        }
                        
                        composable(Screen.Home.route) {
                            MainScreen(
                                onNavigateToDetail = { userId ->
                                    navController.navigate(Screen.Detail.createRoute(userId))
                                }
                            )
                        }
                        
                        composable(
                            route = Screen.Detail.route,
                            arguments = listOf(
                                navArgument("userId") { type = NavType.IntType }
                            )
                        ) { backStackEntry ->
                            val userId = backStackEntry.arguments?.getInt("userId") ?: 0
                            DetailScreen(userId = userId, navController = navController)
                        }
                    }
                }
            }
        }
    }
}

