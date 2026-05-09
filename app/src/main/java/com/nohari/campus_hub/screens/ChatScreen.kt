package com.nohari.campus_hub.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.nohari.campus_hub.data.ChatViewModel
import com.nohari.campus_hub.models.Message


@Composable
fun ChatScreen(receiverId: String) {

    val vm = remember { ChatViewModel() }
    val messages = remember { mutableStateListOf<Message>() }
    val listState = rememberLazyListState()

    var text by remember { mutableStateOf("") }

    LaunchedEffect(receiverId) {

        vm.listenMessages(receiverId) {
            messages.clear()
            messages.addAll(it)
        }
    }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.lastIndex)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {

        LazyColumn(
            modifier = Modifier.weight(1f),
            state = listState
        ) {

            items(messages) { msg ->

                val isMe = msg.senderId ==
                        FirebaseAuth.getInstance().currentUser?.uid

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp),
                    color = if (isMe)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = msg.text,
                        modifier = Modifier.padding(10.dp),
                        color = if (isMe) Color.White else Color.Black
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {

            TextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Message...") }
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    if (text.isNotBlank()) {
                        vm.sendMessage(receiverId, text.trim())
                        text = ""
                    }
                }
            ) {
                Text("Send")
            }
        }
    }
}
