package com.nohari.campus_hub.Screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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

    // 🔥 Load user data
    LaunchedEffect(true) {
        val uid = auth.currentUser?.uid

        if (uid != null) {
            val doc = db.collection("users").document(uid).get().await()
            fullName = doc.getString("fullName") ?: "Student"
        }

        // Sample data (replace later with Firestore)
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
            .padding(16.dp)
    ) {

        // 👋 GREETING
        item {
            Text(
                text = "Welcome, $fullName 👋",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        // ⚡ QUICK ACTIONS
        item {
            Text("Quick Actions", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Button(
                    onClick = {
                        navController.navigate(Routes.ADD_ITEM)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("➕ Add Item")
                }

                Button(
                    onClick = {
                        navController.navigate(Routes.MARKETPLACE)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("📋 View Items")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // 📊 DASHBOARD CARDS
        item {
            Text("Dashboard", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DashboardCard("Assignments")
                DashboardCard("Announcements")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // 📢 ANNOUNCEMENTS
        item {
            Text("Announcements", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(announcements) { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text(
                    text = item,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }

        // 📁 ASSIGNMENTS
        item {
            Text("Assignments", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(assignments) { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text(
                    text = item,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }

        // 🚪 LOGOUT
        item {
            Button(
                onClick = {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Logout")
            }
        }
    }
}

@Composable
fun DashboardCard(title: String) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .height(80.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            Text(title)
        }
    }
}