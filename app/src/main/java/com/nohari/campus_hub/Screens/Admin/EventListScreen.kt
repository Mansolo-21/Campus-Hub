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
import com.nohari.campus_hub.Data.EventViewModel
import com.nohari.campus_hub.models.Event
import com.nohari.campus_hub.utils.RoleManager

@Composable
fun EventListScreen(navController: NavHostController) {

    val vm: EventViewModel = viewModel()

    var events by remember { mutableStateOf(listOf<Event>()) }
    var role by remember { mutableStateOf<String?>(null) }
    var eventToDelete by remember { mutableStateOf<Event?>(null) }

    // ✅ Load data + role safely
    LaunchedEffect(Unit) {
        vm.getEvents { list ->
            events = list
        }

        RoleManager.getUserRole { userRole ->
            role = userRole
        }
    }

    Scaffold(
        floatingActionButton = {
            // ✅ FAB shows only when role is confirmed admin
            if (role == "admin") {
                FloatingActionButton(
                    onClick = { navController.navigate("add_event") }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Event")
                }
            }
        }
    ) { paddingValues ->

        if (events.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            ) {
                Text(
                    text = "No events available",
                    modifier = Modifier.padding(16.dp)
                )
            }
        } else {

            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                items(events, key = { it.id }) { event ->

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {

                        Column(modifier = Modifier.padding(16.dp)) {

                            Text(
                                text = event.title,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(event.description)

                            Spacer(modifier = Modifier.height(6.dp))

                            Text("📅 ${event.date}")

                            // Admin delete
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
                                                eventToDelete = event
                                            }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 🔴 Delete dialog
        if (eventToDelete != null) {
            AlertDialog(
                onDismissRequest = { eventToDelete = null },
                title = { Text("Delete Event") },
                text = { Text("Are you sure you want to delete this event?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            eventToDelete?.let {
                                vm.deleteEvent(it.id)

                                // refresh list
                                vm.getEvents { updated ->
                                    events = updated
                                }
                            }
                            eventToDelete = null
                        }
                    ) {
                        Text("Delete", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { eventToDelete = null }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}