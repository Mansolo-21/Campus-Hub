package com.nohari.campus_hub.screens.Announcements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.nohari.campus_hub.models.Announcement
import com.nohari.campus_hub.navigation.Routes
import com.nohari.campus_hub.utils.RoleManager

@Composable
fun AnnouncementsScreen(navController: NavController) {

    val db = FirebaseFirestore.getInstance()

    var announcements by remember {
        mutableStateOf<List<Announcement>>(emptyList())
    }

    var role by remember {
        mutableStateOf<String?>(null)
    }

    LaunchedEffect(Unit) {

        // Get current user role
        RoleManager.getUserRole {
            role = it
        }

        // Load announcements + assignments
        db.collection("announcements")
            .addSnapshotListener { snapshot, _ ->

                if (snapshot != null) {

                    announcements = snapshot.documents.map { doc ->

                        Announcement(
                            id = doc.id,
                            title = doc.getString("title") ?: "",
                            message = doc.getString("message") ?: "",
                            timestamp = doc.getString("timestamp") ?: "",
                            type = doc.getString("type") ?: "announcement"
                        )
                    }
                }
            }
    }

    Scaffold(

        floatingActionButton = {

            // ONLY ADMIN CAN SEE BUTTON
            if (role == "admin") {

                FloatingActionButton(
                    onClick = {
                        navController.navigate(Routes.ADD_ANNOUNCEMENT)
                    }
                ) {

                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Announcement"
                    )
                }
            }
        }

    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF6F7FB))
        ) {

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                // HEADER
                item {

                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {

                            Text(
                                text = "Campus Feed 📢",
                                style = MaterialTheme.typography.headlineMedium,
                                color = Color.White
                            )

                            Text(
                                text = "Latest announcements & assignments",
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }

                // FEED ITEMS
                items(
                    announcements,
                    key = { it.id }
                ) { item ->

                    AnnouncementCard(item)
                }

                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}

@Composable
fun AnnouncementCard(item: Announcement) {

    val isAssignment = item.type == "assignment"

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = null,

                    tint = if (isAssignment)
                        MaterialTheme.colorScheme.tertiary
                    else
                        MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column {

                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.titleMedium
                    )

                    if (isAssignment) {

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Assignment",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = item.message,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = item.timestamp,
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
        }
    }
}