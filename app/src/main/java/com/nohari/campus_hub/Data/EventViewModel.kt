package com.nohari.campus_hub.Data

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.nohari.campus_hub.models.Event

class EventViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    fun addEvent(
        title: String,
        description: String,
        date: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {

        if (title.isBlank() || description.isBlank() || date.isBlank()) {
            onError("Fill all fields")
            return
        }

        val id = db.collection("events").document().id

        val event = Event(id, title, description, date)

        db.collection("events")
            .document(id)
            .set(event)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onError(it.message ?: "Failed to add event")
            }
    }

    fun getEvents(onResult: (List<Event>) -> Unit) {
        db.collection("events")
            .get()
            .addOnSuccessListener { result ->
                val list = result.documents.mapNotNull {
                    it.toObject(Event::class.java)?.copy(id = it.id)
                }
                onResult(list)
            }
    }

    fun deleteEvent(id: String) {
        db.collection("events").document(id).delete()
    }
}