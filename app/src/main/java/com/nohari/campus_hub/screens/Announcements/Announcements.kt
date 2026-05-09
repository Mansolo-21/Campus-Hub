package com.nohari.campus_hub.screens.Announcements

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
import com.google.firebase.firestore.FirebaseFirestore
import com.nohari.campus_hub.AppCard
import com.nohari.campus_hub.models.Announcement

@Composable
fun AnnouncementsScreen() {

    val db = FirebaseFirestore.getInstance()

    var announcements by remember {
        mutableStateOf<List<Announcement>>(emptyList())
    }

    LaunchedEffect(Unit) {

        db.collection("announcements")
            .addSnapshotListener { snapshot, _ ->

                if (snapshot != null) {

                    val list = snapshot.documents.mapNotNull { doc ->

                        Announcement(
                            title = doc.getString("title") ?: "",
                            message = doc.getString("message") ?: "",
                            timestamp = doc.getString("timestamp") ?: "",
                            type = doc.getString("type") ?: "announcement"
                        )
                    }

                    announcements = list
                }
            }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        item {
            Text(
                text = "📢 Campus Feed",
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

    val isAssignment = item.type == "assignment"

    AppCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {

        Column {

            Row(verticalAlignment = Alignment.CenterVertically) {

                Icon(
                    Icons.Default.Notifications,
                    contentDescription = null,
                    tint = if (isAssignment)
                        MaterialTheme.colorScheme.tertiary
                    else
                        MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = item.message,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Posted: ${item.timestamp}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}