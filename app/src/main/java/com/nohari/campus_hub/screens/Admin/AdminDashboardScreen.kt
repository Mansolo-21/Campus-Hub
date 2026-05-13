package com.nohari.campus_hub.screens.Admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.nohari.campus_hub.navigation.Routes
import com.nohari.campus_hub.utils.RoleManager

private val DeepBlue = Color(0xFF0D47A1)
private val MidBlue = Color(0xFF1976D2)
private val LightBlueBg = Color(0xFFEAF2FF)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(navController: NavController) {

    var role by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        RoleManager.getUserRole {
            role = it
        }
    }

    if (role != "admin") {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(LightBlueBg),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Access Denied",
                color = DeepBlue,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
        return
    }

    Scaffold(
        containerColor = LightBlueBg
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // 🔵 HEADER
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = DeepBlue),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    Text(
                        text = "Campus Hub Admin",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Manage teachers, events, announcements & users",
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }

            Text(
                text = "Dashboard",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = DeepBlue
            )

            // 📌 GRID STYLE MENU
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                DashboardCard(
                    title = "Create Teacher",
                    icon = Icons.Default.AccountBox,
                    color = MidBlue
                ) {
                    navController.navigate(Routes.CREATE_TEACHER)
                }

                DashboardCard(
                    title = "Manage Announcements",
                    icon = Icons.Default.Star,
                    color = DeepBlue
                ) {
                    navController.navigate("announcements")
                }

                DashboardCard(
                    title = "Manage Events",
                    icon = Icons.Default.DateRange,
                    color = MidBlue
                ) {
                    navController.navigate("events")
                }

                DashboardCard(
                    title = "View Users",
                    icon = Icons.Default.Person,
                    color = DeepBlue
                ) {
                    navController.navigate("users")
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // 🚪 LOGOUT CARD
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Text(
                        "Signed in as Admin",
                        color = DeepBlue,
                        fontWeight = FontWeight.Medium
                    )

                    Button(
                        onClick = {
                            FirebaseAuth.getInstance().signOut()
                            navController.navigate(Routes.LOGIN) {
                                popUpTo(0)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DeepBlue),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Logout", color = Color.White)
                    }
                }
            }
        }
    }
}
@Composable
fun DashboardCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(6.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Surface(
                shape = RoundedCornerShape(14.dp),
                color = color
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.padding(12.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
        }
    }
}