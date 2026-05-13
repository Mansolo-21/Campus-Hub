package com.nohari.campus_hub.screens.Events

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.nohari.campus_hub.Components.EventCard
import com.nohari.campus_hub.data.EventViewModel
import com.nohari.campus_hub.models.EventUiState
import com.nohari.campus_hub.utils.RoleManager

@Composable
fun EventsScreen(navController: NavHostController) {

    val vm: EventViewModel = viewModel()
    val state by vm.uiState

    var role by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        RoleManager.getUserRole { userRole ->
            role = userRole
        }
    }

    Scaffold(

        floatingActionButton = {
            if (role == "admin") {
                FloatingActionButton(
                    onClick = { navController.navigate("add_event") },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Event")
                }
            }
        }

    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF6F7FB))
        ) {

            when (val currentState = state) {

                is EventUiState.Loading -> {

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is EventUiState.Empty -> {

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No campus events available")
                    }
                }

                is EventUiState.Error -> {

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(currentState.message)
                    }
                }

                is EventUiState.Success -> {

                    val events = currentState.events

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        // HEADER CARD
                        item {

                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {

                                Column(modifier = Modifier.padding(16.dp)) {

                                    Text(
                                        text = "Campus Events 🎓",
                                        style = MaterialTheme.typography.headlineMedium,
                                        color = Color.White
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = "Stay updated with all campus activities",
                                        color = Color.White.copy(alpha = 0.85f)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                        }

                        // EVENTS LIST
                        items(
                            items = events,
                            key = { it.id.ifBlank { it.hashCode().toString() } }
                        ) { event ->

                            EventCard(
                                event = event,
                                isAdmin = role == "admin",
                                onDelete = if (role == "admin") {
                                    { vm.deleteEvent(event.id) }
                                } else null
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(80.dp))
                        }
                    }
                }
            }
        }
    }
}