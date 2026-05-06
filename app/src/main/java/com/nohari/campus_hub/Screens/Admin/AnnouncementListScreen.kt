package com.nohari.campus_hub.Screens.Admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.nohari.campus_hub.Data.AnnouncementViewModel
import com.nohari.campus_hub.models.Announcement
import com.nohari.campus_hub.utils.RoleManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnnouncementListScreen(navController: NavHostController) {

    val viewModel: AnnouncementViewModel = viewModel()

    val announcements = remember { mutableStateListOf<Announcement>() }
    var role by remember { mutableStateOf<String?>(null) }
    var announcementToDelete by remember { mutableStateOf<Announcement?>(null) }

    // Load data
    LaunchedEffect(Unit) {
        viewModel.getAnnouncements(announcements)
        RoleManager.getUserRole { role = it }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Announcements") })

        },
        floatingActionButton = {
            if (role == "admin") {
                FloatingActionButton(
                    onClick = { navController.navigate("add_announcement") }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Announcement")
                }
            }
        }
    ) { padding ->

        if (announcements.isEmpty()) {

            // ✅ Empty state
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                Text(
                    text = "No announcements available",
                    modifier = Modifier.padding(16.dp)
                )
            }

        } else {

            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                items(announcements, key = { it.id }) { item ->

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {

                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {

                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = item.message,
                                style = MaterialTheme.typography.bodyMedium
                            )

                            // ✅ Admin controls
                            if (role == "admin") {

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {

                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete",
                                        tint = MaterialTheme.colorScheme.error,
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clickable {
                                                announcementToDelete = item
                                            }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 🔴 Delete Confirmation Dialog
        if (announcementToDelete != null) {
            AlertDialog(
                onDismissRequest = { announcementToDelete = null },
                title = { Text("Delete Announcement") },
                text = { Text("Are you sure you want to delete this announcement?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            announcementToDelete?.let {
                                viewModel.deleteAnnouncement(it.id)
                                announcements.remove(it) // instant UI update
                            }
                            announcementToDelete = null
                        }
                    ) {
                        Text("Delete", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { announcementToDelete = null }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}