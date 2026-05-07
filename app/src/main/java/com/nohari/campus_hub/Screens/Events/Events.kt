package com.nohari.campus_hub.Screens.Events

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.nohari.campus_hub.Components.EventCard
import com.nohari.campus_hub.Data.EventViewModel
import com.nohari.campus_hub.models.EventUiState
import com.nohari.campus_hub.utils.RoleManager

@Composable
fun EventsScreen(
    navController: NavHostController
) {

    val vm: EventViewModel = viewModel()
    val state by vm.uiState

    var role by remember { mutableStateOf<String?>(null) }

    // Load user role once
    LaunchedEffect(Unit) {
        RoleManager.getUserRole { userRole ->
            role = userRole
        }
    }

    Scaffold(

        floatingActionButton = {

            // ONLY ADMIN SEES PLUS BUTTON
            if (role == "admin") {

                FloatingActionButton(
                    onClick = {
                        navController.navigate("add_event")
                    }
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Add Event"
                    )
                }
            }
        }

    ) { padding ->

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
                        .padding(padding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {

                    item {
                        Text(
                            text = "Campus Events",
                            style = MaterialTheme.typography.headlineLarge
                        )
                    }

                    items(events, key = { it.id }) { event ->

                        EventCard(
                            event = event,
                            isAdmin = (role == "admin"),
                            onDelete = if (role == "admin") {
                                { vm.deleteEvent(event.id) }
                            } else null
                        )
                    }
                }
            }
        }
    }
}