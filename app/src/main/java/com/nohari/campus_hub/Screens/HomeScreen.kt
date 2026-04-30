package com.nohari.campus_hub.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nohari.campus_hub.navigation.Routes
import kotlinx.coroutines.tasks.await

@Composable
fun HomeScreen(navController: NavController) {

    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    var fullName by remember { mutableStateOf("Student") }
    var announcements by remember { mutableStateOf(listOf<String>()) }
    var assignments by remember { mutableStateOf(listOf<String>()) }

    LaunchedEffect(true) {
        val uid = auth.currentUser?.uid

        if (uid != null) {
            val doc = db.collection("users").document(uid).get().await()
            fullName = doc.getString("fullName") ?: "Student"
        }

        announcements = listOf(
            "Math CAT postponed",
            "Fee deadline extended",
            "New timetable released"
        )

        assignments = listOf(
            "Android Assignment - Due Friday",
            "Database Project - Due Monday"
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
    ) {

        /* ---------------- HERO HEADER ---------------- */
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(20.dp)
            ) {
                Text(
                    text = "Hi, $fullName 👋",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Campus Hub Dashboard",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        /* ---------------- QUICK ACTIONS ---------------- */
        item {
            SectionCard(title = "Quick Actions") {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    ActionTile(
                        title = "Add Item",
                        icon = Icons.Default.Add,
                        color = MaterialTheme.colorScheme.primaryContainer,
                        onClick = { navController.navigate(Routes.ADDITEM) },
                        modifier = Modifier.weight(1f)
                    )

                    ActionTile(
                        title = "Marketplace",
                        icon = Icons.Default.ShoppingCart,
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        onClick = { navController.navigate(Routes.ITEMLIST) },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    ActionTile(
                        title = "Announcements",
                        icon = Icons.Default.Info,
                        color = MaterialTheme.colorScheme.tertiaryContainer,
                        onClick = { navController.navigate(Routes.ANNOUNCEMENTS) },
                        modifier = Modifier.weight(1f)
                    )

                    ActionTile(
                        title = "Events",
                        icon = Icons.Default.DateRange,
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        onClick = { navController.navigate(Routes.EVENTS) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        /* ---------------- DASHBOARD ---------------- */
        item {
            SectionCard(title = "Overview") {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    StatCard(
                        title = "Assignments",
                        value = assignments.size,
                        icon = Icons.Default.Create,
                        modifier = Modifier.weight(1f)
                    )

                    StatCard(
                        title = "Announcements",
                        value = announcements.size,
                        icon = Icons.Default.Notifications,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        /* ---------------- ANNOUNCEMENTS ---------------- */
        item {
            SectionCard(title = "Announcements") {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    announcements.forEach {
                        ListItemCard(Icons.Default.Notifications, it)
                    }
                }
            }
        }

        /* ---------------- ASSIGNMENTS ---------------- */
        item {
            SectionCard(title = "Assignments") {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    assignments.forEach {
                        ListItemCard(Icons.Default.Create, it)
                    }
                }
            }
        }

        /* ---------------- LOGOUT ---------------- */
        item {
            Spacer(modifier = Modifier.height(20.dp))

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
                Icon(Icons.Default.AccountCircle, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Logout")
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

/* ---------------- SECTION WRAPPER (PRO STYLE) ---------------- */

@Composable
fun SectionCard(title: String, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .border(
                1.dp,
                MaterialTheme.colorScheme.outlineVariant,
                shape = MaterialTheme.shapes.large
            )
            .padding(16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(12.dp))

        content()
    }
}

/* ---------------- ACTION TILES ---------------- */

@Composable
fun ActionTile(
    title: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.height(100.dp),
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null)
            Spacer(modifier = Modifier.height(6.dp))
            Text(title)
        }
    }
}

/* ---------------- STATS ---------------- */

@Composable
fun StatCard(
    title: String,
    value: Int,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(110.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null)
            Spacer(modifier = Modifier.height(6.dp))
            Text(title)
            Text(
                text = value.toString(),
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}

/* ---------------- LIST ITEMS ---------------- */

@Composable
fun ListItemCard(icon: ImageVector, text: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null)
            Spacer(modifier = Modifier.width(10.dp))
            Text(text)
        }
    }
}