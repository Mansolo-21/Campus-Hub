package com.nohari.campus_hub.Screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nohari.campus_hub.Data.ChatViewModel
import com.nohari.campus_hub.models.Message


@Composable
fun ChatScreen(receiverId: String) {

    val vm = remember { ChatViewModel() }
    val messages = remember { mutableStateListOf<Message>() }

    var text by remember { mutableStateOf("") }

    val listState = rememberLazyListState()

    // 🔥 Load messages
    LaunchedEffect(Unit) {
        vm.listenMessages(receiverId) {
            messages.clear()
            messages.addAll(it)
        }
    }

    // 🔥 Auto scroll
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.lastIndex)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        // 💬 CHAT LIST
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(messages) { msg ->

                val isMe = msg.senderId ==
                        com.google.firebase.auth.FirebaseAuth
                            .getInstance()
                            .currentUser?.uid

                ChatBubble(msg, isMe)
            }
        }

        // ✍️ INPUT BAR (MODERN STYLE)
        Surface(
            tonalElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                TextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Type a message...") },
                    shape = MaterialTheme.shapes.large
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        if (text.isNotBlank()) {
                            vm.sendMessage(receiverId, text.trim())
                            text = ""
                        }
                    },
                    shape = MaterialTheme.shapes.large
                ) {
                    Text("Send")
                }
            }
        }
    }
}
@Composable
fun ChatBubble(message: Message, isMe: Boolean) {

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {

        Surface(
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isMe) 16.dp else 0.dp,
                bottomEnd = if (isMe) 0.dp else 16.dp
            ),
            color = if (isMe)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.surfaceVariant
        ) {
            Text(
                text = message.text,
                modifier = Modifier.padding(12.dp),
                color = if (isMe) Color.White else Color.Black
            )
        }
    }
}