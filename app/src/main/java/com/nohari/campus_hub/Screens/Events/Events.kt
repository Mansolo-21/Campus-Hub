package com.nohari.campus_hub.Screens.Events

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class Event(
    val title: String,
    val date: String,
    val location: String
)

@Composable
fun EventsScreen() {

    var events by remember {
        mutableStateOf(
            listOf(
                Event("Tech Day", "5 May 2026", "Main Hall"),
                Event("Sports Day", "10 May 2026", "Campus Grounds"),
                Event("Career Fair", "15 May 2026", "Auditorium")
            )
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        item {
            Text(
                text = "📅 Events",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        items(events) { item ->
            EventCard(item)
        }
    }
}

@Composable
fun EventCard(item: Event) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {

            Row {
                Icon(Icons.Default.Star, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(item.title, style = MaterialTheme.typography.titleMedium)
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text("📍 ${item.location}")
            Text("📆 ${item.date}")
        }
    }
}