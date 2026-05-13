package com.nohari.campus_hub.screens.Events

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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

private val DeepBlue = Color(0xFF0D47A1)
private val MidBlue = Color(0xFF1976D2)
private val LightBlueBg = Color(0xFFEAF2FF)

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
        containerColor = LightBlueBg,

        floatingActionButton = {
            if (role == "admin") {
                FloatingActionButton(
                    onClick = { navController.navigate("add_event") },
                    containerColor = DeepBlue,
                    contentColor = Color.White,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Event")
                }
            }
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            when (val currentState = state) {

                is EventUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = MidBlue)
                    }
                }

                is EventUiState.Empty -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "No campus events yet",
                            color = DeepBlue,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }

                is EventUiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            currentState.message,
                            color = Color.Red
                        )
                    }
                }

                is EventUiState.Success -> {

                    val events = currentState.events

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {

                        // 🔵 HEADER
                        item {
                            Card(
                                shape = RoundedCornerShape(20.dp),
                                colors = CardDefaults.cardColors(containerColor = DeepBlue),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(18.dp)
                                ) {
                                    Text(
                                        text = "Campus Events ",
                                        style = MaterialTheme.typography.headlineMedium,
                                        color = Color.White
                                    )

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Text(
                                        text = "Stay updated with lectures, activities & campus life",
                                        color = Color.White.copy(alpha = 0.85f)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        // 📌 EVENTS
                        items(
                            items = events,
                            key = { it.id.ifBlank { it.hashCode().toString() } }
                        ) { event ->

                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White
                                ),
                                elevation = CardDefaults.cardElevation(6.dp)
                            ) {
                                EventCard(
                                    event = event,
                                    isAdmin = role == "admin",
                                    onDelete = if (role == "admin") {
                                        { vm.deleteEvent(event.id) }
                                    } else null
                                )
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(90.dp))
                        }
                    }
                }
            }
        }
    }
}