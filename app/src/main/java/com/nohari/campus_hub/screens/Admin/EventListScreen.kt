package com.nohari.campus_hub.screens.Admin

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
import com.nohari.campus_hub.data.EventViewModel
import com.nohari.campus_hub.models.Event
import com.nohari.campus_hub.models.EventUiState

@Composable
fun EventListScreen(navController: NavHostController) {

    val vm: EventViewModel = viewModel()
    val state by vm.uiState

    var eventToDelete by remember {
        mutableStateOf<Event?>(null)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("add_event")
                }
            ) {
                Icon(Icons.Default.Add, null)
            }
        }
    ) { padding ->

        when (state) {

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
                    Text("No events found")
                }
            }

            is EventUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text((state as EventUiState.Error).message)
                }
            }

            is EventUiState.Success -> {

                val events = (state as EventUiState.Success).events

                LazyColumn(
                    modifier = Modifier
                        .padding(padding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {

                    item {
                        Text(
                            text = "Manage Events",
                            style = MaterialTheme.typography.headlineLarge
                        )
                    }

                    items(events, key = { it.id }) { event ->

                        EventCard(
                            event = event,
                            isAdmin = true,
                            onDelete = {
                                eventToDelete = event
                            }
                        )
                    }
                }
            }
        }

        if (eventToDelete != null) {

            AlertDialog(
                onDismissRequest = {
                    eventToDelete = null
                },
                title = {
                    Text("Delete Event")
                },
                text = {
                    Text("This action cannot be undone")
                },
                confirmButton = {TextButton(
                    onClick = {
                        vm.deleteEvent(eventToDelete!!.id)
                        eventToDelete = null
                    }
                ) {
                    Text("Delete")
                }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            eventToDelete = null
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}