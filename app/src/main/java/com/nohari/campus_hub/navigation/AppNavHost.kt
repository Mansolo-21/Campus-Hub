package com.nohari.campus_hub.navigation

import android.R
import com.nohari.campus_hub.Screens.Admin.AddAnnouncementScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.nohari.campus_hub.Screens.Admin.AddEventScreen
import com.nohari.campus_hub.Screens.Admin.AdminDashboardScreen
import com.nohari.campus_hub.Screens.Admin.AnnouncementListScreen
import com.nohari.campus_hub.Screens.Announcements.AnnouncementsScreen
import com.nohari.campus_hub.Screens.ChatScreen
import com.nohari.campus_hub.Screens.CreateTeacherScreen
import com.nohari.campus_hub.Screens.Events.EventsScreen

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
        composable(Routes.ANNOUNCEMENTS) {
            AnnouncementsScreen()
        }

        composable(Routes.EVENTS) {
            EventsScreen()
        }
        composable(Routes.ADMIN_DASHBOARD) {
            AdminDashboardScreen(navController)
        }

        composable(Routes.CREATE_TEACHER) {
            CreateTeacherScreen(navController)
        }
        composable(Routes.ANNOUNCEMENTS) {
            AnnouncementListScreen(navController)
        }

        composable(Routes.ADD_ANNOUNCEMENT) {
            AddAnnouncementScreen(navController)
        }
        composable(Routes.ADD_EVENT){
            AddEventScreen(navController)
        }
    }
}