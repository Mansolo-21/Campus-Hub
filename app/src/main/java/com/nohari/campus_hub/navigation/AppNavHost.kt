package com.nohari.campus_hub.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.nohari.campus_hub.Screens.Admin.AddEventScreen
import com.nohari.campus_hub.Screens.Admin.AdminDashboardScreen
import com.nohari.campus_hub.Screens.Admin.AnnouncementListScreen
import com.nohari.campus_hub.Screens.Admin.CreateTeacherScreen
import com.nohari.campus_hub.Screens.Announcements.AddAnnouncementScreen
import com.nohari.campus_hub.Screens.Announcements.AnnouncementsScreen
import com.nohari.campus_hub.Screens.Auth.LoginScreen
import com.nohari.campus_hub.Screens.Auth.RegisterScreen
import com.nohari.campus_hub.Screens.ChatListScreen
import com.nohari.campus_hub.Screens.ChatScreen
import com.nohari.campus_hub.Screens.Events.EventsScreen
import com.nohari.campus_hub.Screens.HomeScreen
import com.nohari.campus_hub.Screens.Marketplace.AddItemScreen
import com.nohari.campus_hub.Screens.Marketplace.ItemDetailScreen
import com.nohari.campus_hub.Screens.Marketplace.ItemListScreen
import com.nohari.campus_hub.Screens.OnboardingScreen
import com.nohari.campus_hub.Screens.ProfileScreen
import com.nohari.campus_hub.Screens.SplashScreen
import com.nohari.campus_hub.Screens.Teacher.AssignmentsScreen
import com.nohari.campus_hub.Screens.UserListScreen
import com.nohari.campus_hub.screens.RegisterCampusScreen


@Composable
fun AppNavHost(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Routes.ONBOARDING
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
        composable(Routes.SPLASH) {
            SplashScreen(navController)
        }
        composable(Routes.ADDITEM) {
            AddItemScreen(navController)
        }
        composable(Routes.ITEMLIST) {
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
            EventsScreen(navController)
        }
        composable(Routes.ADMIN_DASHBOARD) {
            AdminDashboardScreen(navController)
        }

        composable("create_teacher") {
            CreateTeacherScreen(
                navController = navController,
                onCreateTeacher = { fullName, email, password ->

                    val auth = com.google.firebase.auth.FirebaseAuth.getInstance()
                    val db = com.google.firebase.firestore.FirebaseFirestore.getInstance()

                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener { result ->

                            val uid = result.user?.uid ?: return@addOnSuccessListener

                            val teacher = hashMapOf(
                                "uid" to uid,
                                "fullName" to fullName,
                                "email" to email,
                                "role" to "teacher"
                            )

                            db.collection("teachers")
                                .document(uid)
                                .set(teacher)
                                .addOnSuccessListener {

                                    android.widget.Toast.makeText(
                                        navController.context,
                                        "Teacher created successfully",
                                        android.widget.Toast.LENGTH_SHORT
                                    ).show()

                                    navController.popBackStack()
                                }
                                .addOnFailureListener {
                                    android.widget.Toast.makeText(
                                        navController.context,
                                        "Firestore error: ${it.message}",
                                        android.widget.Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }
                        .addOnFailureListener {
                            android.widget.Toast.makeText(
                                navController.context,
                                "Auth error: ${it.message}",
                                android.widget.Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            )
        }
        composable(Routes.ANNOUNCEMENTS) {
            AnnouncementListScreen(navController)
        }

        composable(Routes.ADD_ANNOUNCEMENT) {
            AddAnnouncementScreen(navController)
        }
        composable(Routes.ADD_EVENT) {
            AddEventScreen(navController)
        }
        composable(Routes.USERS) {
            UserListScreen(navController)
        }
        composable(Routes.ASSIGNMENT) {
            AssignmentsScreen(navController)
        }

        composable(Routes.PROFILE) {
            ProfileScreen(navController)
        }
        composable(Routes.CHAT_LIST) {
            ChatListScreen(navController, users = emptyList<com.nohari.campus_hub.models.User>())
        }

        composable("chat/{receiverId}") { backStackEntry ->
            val receiverId = backStackEntry.arguments?.getString("receiverId") ?: ""
            ChatScreen(receiverId)
        }
        composable(Routes.ONBOARDING){
            OnboardingScreen(navController)
        }
        composable(Routes.REGISTER_CAMPUS){
            RegisterCampusScreen(navController)
        }

    }
}