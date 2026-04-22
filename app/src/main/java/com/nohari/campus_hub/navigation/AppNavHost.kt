package com.nohari.campus_hub.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.*

import com.nohari.campus_hub.Screens.Auth.LoginScreen
import com.nohari.campus_hub.Screens.Auth.RegisterScreen
import com.nohari.campus_hub.Screens.HomeScreen

@Composable
fun AppNavHost(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {

        composable(Routes.LOGIN) {
            LoginScreen(navController)
        }

        composable(Routes.REGISTER) {
            RegisterScreen(navController)
        }

        composable(Routes.HOME) {
            HomeScreen(navController)
        }
    }
}