package com.nohari.campus_hub.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nohari.campus_hub.models.User


@Composable
fun ChatListScreen(
    navController: NavController,
    users: List<User>
) {

    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()

    var users by remember { mutableStateOf(listOf<User>()) }
    val currentUserId = auth.currentUser?.uid

    var currentUserRole by remember { mutableStateOf("student") }

    // 1. Get current user role
    LaunchedEffect(Unit) {
        currentUserId?.let { uid ->
            db.collection("users").document(uid)
                .get()
                .addOnSuccessListener {
                    currentUserRole = it.getString("role") ?: "student"
                }
        }
    }

    // 2. Load users
    LaunchedEffect(currentUserRole) {

        db.collection("users")
            .addSnapshotListener { snapshot, _ ->

                val list = snapshot?.documents?.mapNotNull { doc ->

                    val user = doc.toObject(User::class.java) ?: return@mapNotNull null

                    if (user.uid == currentUserId) return@mapNotNull null

                    // ROLE FILTERING LOGIC
                    when (currentUserRole) {

                        "student" -> if (user.role == "teacher") user else null
                        "teacher" -> if (user.role == "student") user else null
                        else -> user
                    }

                }?.filterNotNull() ?: emptyList()

                users = list
            }
    }

    Column(modifier = Modifier.fillMaxSize()) {

        Text(
            text = "Chats",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(16.dp)
        )

        LazyColumn {

            items(users) { user ->

                ChatUserItem(
                    user = user,
                    onClick = {
                        navController.navigate("chat/${user.uid}")
                    }
                )
            }
        }
    }
}
@Composable
fun ChatUserItem(
    user: User,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(6.dp)
    ) {

        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Avatar
            Surface(
                modifier = Modifier.size(45.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
            ) {

                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = user.fullName.firstOrNull()?.toString() ?: "?",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {

                Text(
                    text = user.fullName,
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    text = user.role,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Text(
                text = "Chat",
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}