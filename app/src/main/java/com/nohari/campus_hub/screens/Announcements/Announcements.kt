package com.nohari.campus_hub.screens.Announcements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.nohari.campus_hub.models.Announcement
import com.nohari.campus_hub.navigation.Routes
import com.nohari.campus_hub.utils.RoleManager

private val DeepBlue = Color(0xFF0D47A1)
private val MidBlue = Color(0xFF1976D2)
private val SoftBg = Color(0xFFEAF2FF)

@Composable
fun AnnouncementsScreen(navController: NavController) {

    val db = FirebaseFirestore.getInstance()

    var announcements by remember { mutableStateOf<List<Announcement>>(emptyList()) }
    var role by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {

        RoleManager.getUserRole { role = it }

        db.collection("announcements")
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    announcements = snapshot.documents.map { doc ->
                        Announcement(
                            id = doc.id,
                            title = doc.getString("title") ?: "",
                            message = doc.getString("message") ?: "",
                            timestamp = doc.getString("timestamp") ?: "",
                            type = doc.getString("type") ?: "announcement"
                        )
                    }
                }
            }
    }

    Scaffold(
        containerColor = SoftBg,

        floatingActionButton = {
            if (role == "admin") {
                FloatingActionButton(
                    onClick = { navController.navigate(Routes.ADD_ANNOUNCEMENT) },
                    containerColor = DeepBlue,
                    contentColor = Color.White,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // 🔵 HEADER
            item {
                Card(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = DeepBlue),
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Column(modifier = Modifier.padding(18.dp)) {

                        Text(
                            text = "Campus Feed 📢",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Latest announcements & assignments",
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))
            }

            // 📌 FEED
            items(
                announcements,
                key = { it.id }
            ) { item ->
                AnnouncementCard(item)
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}
@Composable
fun AnnouncementCard(item: Announcement) {

    val isAssignment = item.type == "assignment"

    val accentColor = if (isAssignment) Color(0xFF7C4DFF) else Color(0xFF1976D2)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(5.dp)
    ) {

        Column(modifier = Modifier.padding(16.dp)) {

            // HEADER ROW
            Row(verticalAlignment = Alignment.CenterVertically) {

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = accentColor.copy(alpha = 0.12f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.padding(10.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {

                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    if (isAssignment) {
                        Text(
                            text = "Assignment",
                            color = accentColor,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // MESSAGE
            Text(
                text = item.message,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF444444)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // TIMESTAMP
            Text(
                text = item.timestamp,
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
        }
    }
}