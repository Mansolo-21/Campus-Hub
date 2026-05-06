package com.nohari.campus_hub.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nohari.campus_hub.utils.RoleManager
import com.nohari.campus_hub.utils.SectionTitle

@Composable
fun HomeScreen(navController: NavController) {

    var role by remember { mutableStateOf("student") }
    var name by remember { mutableStateOf("Student") }

    LaunchedEffect(Unit) {
        RoleManager.getUserRole { role = it }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {

        item {
            Text(
                "Welcome ",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                name,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(Modifier.height(20.dp))
        }

        // QUICK ACTIONS
        item {
            SectionTitle("Quick Actions")
            Spacer(Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {

                ActionTile("Marketplace") {
                    navController.navigate("itemlist")
                }

                ActionTile("Announcements") {
                    navController.navigate("announcements")
                }
            }

            Spacer(Modifier.height(20.dp))
        }

        // ROLE BASED
        item {
            if (role == "admin") {
                SectionTitle("Admin")
                Spacer(Modifier.height(10.dp))

                ActionTile("Admin Dashboard") {
                    navController.navigate("admin_dashboard")
                }
            }

            if (role == "teacher") {
                SectionTitle("Teacher")
                Spacer(Modifier.height(10.dp))

                ActionTile("Assignments") { }
            }
        }
    }
}