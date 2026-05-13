package com.nohari.campus_hub.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nohari.campus_hub.models.User

private val DeepBlue = Color(0xFF0D47A1)
private val MidBlue = Color(0xFF1976D2)
private val SoftBg = Color(0xFFEAF2FF)

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

    var chats by remember { mutableStateOf(listOf<ChatPreview>()) }

    // 📌 LOAD USERS + CHAT PREVIEW
    LaunchedEffect(Unit) {

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftBg)
    ) {

        // 🔵 HEADER
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = DeepBlue)
        ) {

            Column(modifier = Modifier.padding(16.dp)) {

                Text(
                    text = "Messages 💬",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Connect with students & staff",
                    color = Color.White.copy(alpha = 0.85f)
                )
            }
        }

        // 💬 CHAT LIST
        LazyColumn(
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            items(chats) { chat ->

                val time = if (chat.lastTimestamp != 0L)
                    android.text.format.DateFormat.format(
                        "hh:mm a",
                        chat.lastTimestamp
                    ).toString()
                else ""

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate("chat/${chat.user.uid}")
                        },
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        // 🧑 AVATAR
                        Surface(
                            modifier = Modifier.size(48.dp),
                            shape = CircleShape,
                            color = MidBlue.copy(alpha = 0.15f)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = chat.user.fullName.firstOrNull()?.uppercase()
                                        ?: "?",
                                    color = DeepBlue,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(Modifier.width(12.dp))

                        // 📌 USER INFO
                        Column(modifier = Modifier.weight(1f)) {

                            Text(
                                text = chat.user.fullName,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black
                            )

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                text = chat.lastMessage.ifEmpty { "No messages yet" },
                                color = Color.Gray,
                                maxLines = 1
                            )
                        }

                        // ⏱ TIME + ACTION
                        Column(
                            horizontalAlignment = Alignment.End
                        ) {

                            Text(
                                text = time,
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = DeepBlue.copy(alpha = 0.1f)
                            ) {
                                Text(
                                    text = "Open",
                                    modifier = Modifier.padding(
                                        horizontal = 8.dp,
                                        vertical = 4.dp
                                    ),
                                    color = DeepBlue,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}