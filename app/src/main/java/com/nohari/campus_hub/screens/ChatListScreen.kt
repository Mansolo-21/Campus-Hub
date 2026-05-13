package com.nohari.campus_hub.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nohari.campus_hub.models.User

data class ChatPreview(
    val user: User,
    val lastMessage: String = "",
    val lastTimestamp: Long = 0L
)

@Composable
fun ChatListScreen(navController: NavController) {

    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()

    val currentUserId = auth.currentUser?.uid

    var currentUserRole by remember { mutableStateOf("student") }
    var chats by remember { mutableStateOf(listOf<ChatPreview>()) }

    // get role
    LaunchedEffect(Unit) {
        currentUserId?.let { uid ->
            db.collection("users").document(uid)
                .get()
                .addOnSuccessListener {
                    currentUserRole = it.getString("role") ?: "student"
                }
        }
    }

    // load users + last messages
    LaunchedEffect(currentUserRole) {

        db.collection("users")
            .addSnapshotListener { snapshot, _ ->

                val users = snapshot?.documents?.mapNotNull {
                    it.toObject(User::class.java)
                }?.filter { it.uid != currentUserId } ?: emptyList()

                val temp = mutableListOf<ChatPreview>()

                users.forEach { user ->

                    val chatId =
                        if (currentUserId!! < user.uid)
                            "$currentUserId-${user.uid}"
                        else
                            "${user.uid}-$currentUserId"

                    db.collection("chats")
                        .document(chatId)
                        .addSnapshotListener { chatSnap, _ ->

                            val preview = ChatPreview(
                                user = user,
                                lastMessage = chatSnap?.getString("lastMessage") ?: "",
                                lastTimestamp = chatSnap?.getLong("lastTimestamp") ?: 0L
                            )

                            temp.removeAll { it.user.uid == user.uid }
                            temp.add(preview)

                            chats = temp.sortedByDescending { it.lastTimestamp }
                        }
                }
            }
    }

    Column(modifier = Modifier.fillMaxSize()) {

        Text(
            "Chats",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(16.dp)
        )

        LazyColumn {
            items(chats) { chat ->

                val time = if (chat.lastTimestamp != 0L)
                    android.text.format.DateFormat.format("hh:mm a", chat.lastTimestamp)
                else ""

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate("chat/${chat.user.uid}")
                        }
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Surface(
                        modifier = Modifier.size(45.dp),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(chat.user.fullName.firstOrNull()?.toString() ?: "?")
                        }
                    }

                    Spacer(Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {

                        Text(chat.user.fullName)

                        Text(
                            text = chat.lastMessage.ifEmpty { "No messages yet" },
                            color = Color.Gray,
                            maxLines = 1
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {

                        Text(
                            text = time.toString(),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray
                        )

                        Text(
                            "Chat",
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}