package com.nohari.campus_hub.Screens.Announcements

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class Announcement(
    val title: String,
    val date: String
)

@Composable
fun AnnouncementsScreen() {

    var announcements by remember {
        mutableStateOf(
            listOf(
                Announcement("Math CAT postponed", "30 Apr 2026"),
                Announcement("Fee deadline extended", "28 Apr 2026"),
                Announcement("New timetable released", "25 Apr 2026")
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
                text = "📢 Announcements",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        items(announcements) { item ->
            AnnouncementCard(item)
        }
    }
}

@Composable
fun AnnouncementCard(item: Announcement) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row {
                Icon(Icons.Default.Notifications, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(item.title, style = MaterialTheme.typography.titleMedium)
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Posted: ${item.date}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}