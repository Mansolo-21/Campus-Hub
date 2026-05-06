package com.nohari.campus_hub.Data

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.nohari.campus_hub.models.Announcement

class AnnouncementViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    fun addAnnouncement(
        title: String,
        message: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {

        if (title.isBlank() || message.isBlank()) {
            onError("Fill all fields")
            return
        }

        val id = db.collection("announcements").document().id

        val announcement = Announcement(
            id = id,
            title = title,
            message = message
        )

        db.collection("announcements")
            .document(id)
            .set(announcement)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onError(it.message ?: "Error posting announcement")
            }
    }

    fun getAnnouncements(list: MutableList<Announcement>) {
        db.collection("announcements")
            .orderBy("timestamp")
            .get()
            .addOnSuccessListener { result ->
                list.clear()
                for (doc in result) {
                    list.add(doc.toObject(Announcement::class.java))
                }
            }
    }

    fun deleteAnnouncement(id: String) {
        db.collection("announcements").document(id).delete()
    }
}