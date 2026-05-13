package com.nohari.campus_hub.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.nohari.campus_hub.models.Message

class ChatViewModel {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun sendMessage(receiverId: String, text: String) {

        val senderId = auth.currentUser?.uid ?: return
        val id = db.collection("messages").document().id

        val message = Message(
            id = id,
            senderId = senderId,
            receiverId = receiverId,
            text = text,
            timestamp = System.currentTimeMillis()
        )

        db.collection("messages")
            .document(id)
            .set(message)

        // optional: last message storage for chat list
        val chatId = if (senderId < receiverId)
            "$senderId-$receiverId"
        else
            "$receiverId-$senderId"

        db.collection("chats")
            .document(chatId)
            .set(
                mapOf(
                    "lastMessage" to text,
                    "lastTimestamp" to System.currentTimeMillis()
                ),
                SetOptions.merge()
            )
    }

    fun listenMessages(
        otherUserId: String,
        onUpdate: (List<Message>) -> Unit
    ) {

        val currentUser = auth.currentUser?.uid ?: return

        db.collection("messages")
            .addSnapshotListener { value, _ ->

                val list = value?.documents?.mapNotNull { doc ->
                    val msg = doc.toObject(Message::class.java) ?: return@mapNotNull null

                    val isBetween =
                        (msg.senderId == currentUser && msg.receiverId == otherUserId) ||
                                (msg.senderId == otherUserId && msg.receiverId == currentUser)

                    if (isBetween) msg else null
                } ?: emptyList()

                onUpdate(list.sortedBy { it.timestamp })
            }
    }
}