package com.nohari.campus_hub.Data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nohari.campus_hub.models.Message

class ChatViewModel {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun sendMessage(receiverId: String, text: String) {

        val senderId = auth.currentUser?.uid ?: return
        val id = db.collection("messages").document().id

        val message = Message(id, senderId, receiverId, text)

        db.collection("messages").document(id).set(message)
    }

    fun listenMessages(
        otherUserId: String,
        onUpdate: (List<Message>) -> Unit
    ) {
        val currentUser = auth.currentUser?.uid ?: return

        db.collection("messages")
            .addSnapshotListener { value, _ ->

                val list = value?.documents?.mapNotNull {
                    val msg = it.toObject(Message::class.java)

                    if (
                        msg?.senderId == currentUser && msg.receiverId == otherUserId ||
                        msg?.senderId == otherUserId && msg.receiverId == currentUser
                    ) msg else null
                } ?: emptyList()

                onUpdate(list.sortedBy { it.timestamp })
            }
    }
}