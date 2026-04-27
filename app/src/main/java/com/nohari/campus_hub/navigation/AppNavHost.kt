package com.nohari.campus_hub.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.*

import com.nohari.campus_hub.Screens.HomeScreen
import com.nohari.campus_hub.Screens.Marketplace.AddItemScreen
import com.nohari.campus_hub.Screens.Marketplace.ItemDetailScreen
import com.nohari.campus_hub.Screens.Marketplace.ItemListScreen
import com.nohari.campus_hub.Screens.SplashScreen
import com.nohari.campus_hub.screens.auth.LoginScreen
import com.nohari.campus_hub.screens.auth.RegisterScreen

@Composable
fun AppNavHost(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH
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
        composable(Routes.SPLASH){
            SplashScreen(navController)
        }
        composable(Routes.ADDITEM){
            AddItemScreen(navController)
        }
        composable(Routes.ITEMLIST){
        ItemListScreen(navController)
    }
        composable("${Routes.ITEM_DETAIL}/{itemId}") { backStackEntry ->

            val itemId = backStackEntry.arguments?.getString("itemId") ?: ""

            ItemDetailScreen(navController, itemId)
        }
    }
}