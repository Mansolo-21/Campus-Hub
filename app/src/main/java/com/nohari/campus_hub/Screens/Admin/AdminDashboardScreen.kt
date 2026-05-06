package com.nohari.campus_hub.Screens.Admin

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.nohari.campus_hub.navigation.Routes
import com.nohari.campus_hub.utils.RoleManager

@Composable
fun AdminDashboardScreen(navController: NavController) {

    var role by remember { mutableStateOf("") }

    // 🔐 Check role
    LaunchedEffect(Unit) {
        RoleManager.getUserRole {
            role = it
        }
    }

    // 🚫 Block non-admins
    if (role != "admin") {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Access Denied")
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Admin Dashboard 👑",
            style = MaterialTheme.typography.headlineMedium
        )

        // 🔹 CREATE TEACHER
        DashboardButton(
            title = "Create Teacher",
            icon = Icons.Default.Create
        ) {
            navController.navigate(Routes.CREATE_TEACHER)
        }

        // 🔹 ANNOUNCEMENTS
        DashboardButton(
            title = "Manage Announcements",
            icon = Icons.Default.Star
        ) {
            navController.navigate("announcements")
        }

        // 🔹 EVENTS
        DashboardButton(
            title = "Manage Events",
            icon = Icons.Default.DateRange
        ) {
            navController.navigate("events")
        }

        // 🔹 VIEW USERS
        DashboardButton(
            title = "View Users",
            icon = Icons.Default.AccountBox
        ) {
            navController.navigate("users")
        }

        Spacer(modifier = Modifier.weight(1f))

        // 🔴 LOGOUT
        Button(
            onClick = {
                FirebaseAuth.getInstance().signOut()
                navController.navigate(Routes.LOGIN) {
                    popUpTo(0)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Icon(Icons.Default.AccountCircle, null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Logout")
        }
    }
}
@Composable
fun DashboardButton(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null)
            Spacer(modifier = Modifier.width(12.dp))
            Text(title)
        }
    }
}