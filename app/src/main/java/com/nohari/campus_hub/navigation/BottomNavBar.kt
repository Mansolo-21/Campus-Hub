package com.nohari.campus_hub.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController

data class BottomNavItem(
    val title: String,
    val route: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

@Composable
fun CampusBottomNavBar(
    navController: NavController,
    currentRoute: String?
) {

    val items = listOf(
        BottomNavItem("Home", "home", Icons.Default.Home),
        BottomNavItem("Chats", "chat_list", Icons.Default.Email),
        BottomNavItem("Events", "events", Icons.Default.DateRange),
        BottomNavItem("Feed", "announcements", Icons.Default.Notifications),
        BottomNavItem("Profile", "profile", Icons.Default.Person)
    )

    NavigationBar(containerColor = Color.White) {

        items.forEach { item ->

            val selected = currentRoute == item.route

            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo("home") { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        item.icon,
                        contentDescription = item.title,
                        tint = if (selected) Color(0xFF1976D2) else Color.Gray
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        color = if (selected) Color(0xFF1976D2) else Color.Gray
                    )
                }
            )
        }
    }
}