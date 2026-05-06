package com.nohari.campus_hub.Screens.Announcements

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nohari.campus_hub.AppCard
import com.nohari.campus_hub.models.Announcement



// 🖥️ SCREEN
@Composable
fun AnnouncementsScreen() {

    var announcements by remember {
        mutableStateOf(
            listOf(
                Announcement(
                    "Math CAT postponed",
                    "The test has been moved to next week.",
                    "30 Apr 2026"
                ),
                Announcement(
                    "Fee deadline extended",
                    "Students now have 1 extra week to pay.",
                    "28 Apr 2026"
                ),
                Announcement(
                    "New timetable released",
                    "Check the portal for the updated schedule.",
                    "25 Apr 2026"
                )
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

    AppCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {

        Column {

            // 🔔 TITLE ROW
            Row(verticalAlignment = Alignment.CenterVertically) {

                Icon(
                    Icons.Default.Notifications,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 📝 MESSAGE
            Text(
                text = item.message,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(6.dp))

            // 📅 DATE
            Text(
                text = "Posted: ${item.timestamp}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}