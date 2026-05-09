package com.nohari.campus_hub.navigation

import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import android.widget.Toast
import androidx.compose.animation.core.snap
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.google.firebase.firestore.FirebaseFirestore
import com.nohari.campus_hub.data.SessionManager
import com.nohari.campus_hub.screens.Admin.AddEventScreen
import com.nohari.campus_hub.screens.Admin.AdminDashboardScreen
import com.nohari.campus_hub.screens.Admin.AnnouncementListScreen
import com.nohari.campus_hub.screens.Admin.CreateTeacherScreen
import com.nohari.campus_hub.screens.Announcements.AddAnnouncementScreen
import com.nohari.campus_hub.screens.Announcements.AnnouncementsScreen
import com.nohari.campus_hub.screens.Auth.LoginScreen
import com.nohari.campus_hub.screens.Auth.RegisterScreen
import com.nohari.campus_hub.screens.CampusListScreen
import com.nohari.campus_hub.screens.ChatListScreen
import com.nohari.campus_hub.screens.ChatScreen
import com.nohari.campus_hub.screens.Events.EventsScreen
import com.nohari.campus_hub.screens.HomeScreen
import com.nohari.campus_hub.screens.Marketplace.AddItemScreen
import com.nohari.campus_hub.screens.Marketplace.ItemDetailScreen
import com.nohari.campus_hub.screens.Marketplace.ItemListScreen
import com.nohari.campus_hub.screens.OnboardingScreen
import com.nohari.campus_hub.screens.ProfileScreen
import com.nohari.campus_hub.screens.SplashScreen
import com.nohari.campus_hub.screens.Teacher.AssignmentsScreen
import com.nohari.campus_hub.screens.UserListScreen
import com.nohari.campus_hub.data.repository.AuthRepository
import com.nohari.campus_hub.models.User
import com.nohari.campus_hub.screens.RegisterCampusScreen


@Composable
fun AppNavHost(navController: NavHostController) {

    val context = LocalContext.current

    var startDestination by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {

        val onboardingDone = SessionManager.isOnboardingDone(context)
        val campusSelected = SessionManager.isCampusSelected(context)

        startDestination = when {

            !onboardingDone -> Routes.ONBOARDING

            !campusSelected -> Routes.ONBOARDING // or CAMPUS_LIST if you want split flow

            else -> Routes.LOGIN
        }
    }

    if (startDestination == null) return

    NavHost(
        navController = navController,
        startDestination = startDestination!!
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

            val scope = rememberCoroutineScope()
            val repo = AuthRepository()

            CreateTeacherScreen(
                navController = navController,
                onCreateTeacher = { fullName, email, password ->

                    scope.launch {

                        val result = repo.registerUser(
                            fullName = fullName,
                            email = email,
                            password = password,
                            role = "teacher"
                        )

                        result.onSuccess {

                            Toast.makeText(
                                navController.context,
                                "Teacher created successfully",
                                Toast.LENGTH_SHORT
                            ).show()

                            navController.popBackStack()

                        }.onFailure {

                            Toast.makeText(
                                navController.context,
                                it.message ?: "Failed",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
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

            val db = FirebaseFirestore.getInstance()

            var users by remember {
                mutableStateOf<List<User>>(emptyList())
            }

            LaunchedEffect(Unit) {
                db.collection("users")
                    .get()
                    .addOnSuccessListener { snap ->
                        users = snap.documents.mapNotNull {
                            it.toObject(User::class.java)
                        }
                    }
            }

            ChatListScreen(
                navController = navController,
                users = users
            )
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
        composable(Routes.CAMPUS_LIST) {
            CampusListScreen(navController)
        }

    }
}

