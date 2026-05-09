package com.nohari.campus_hub.data

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.nohari.campus_hub.models.Event
import com.nohari.campus_hub.models.EventUiState
import java.util.UUID

class EventViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    var uiState = mutableStateOf<EventUiState>(EventUiState.Loading)
        private set

    init {
        fetchEvents()
    }

    fun fetchEvents() {

        uiState.value = EventUiState.Loading

        db.collection("events")
            .get()
            .addOnSuccessListener { result ->

                val events = result.documents.mapNotNull {
                    it.toObject(Event::class.java)
                }

                uiState.value = if (events.isEmpty()) {
                    EventUiState.Empty
                } else {
                    EventUiState.Success(events)
                }
            }
            .addOnFailureListener {
                uiState.value = EventUiState.Error(it.message ?: "Unknown error")
            }
    }

    fun addEvent(
        title: String,
        description: String,
        date: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {

        val id = UUID.randomUUID().toString()

        val event = Event(
            id = id,
            title = title,
            description = description,
            date = date
        )

        db.collection("events")
            .document(id)
            .set(event)
            .addOnSuccessListener {
                fetchEvents()
                onSuccess()
            }
            .addOnFailureListener {
                onError(it.message ?: "Failed to save")
            }
    }

    fun deleteEvent(id: String) {

        db.collection("events")
            .document(id)
            .delete()
            .addOnSuccessListener {
                fetchEvents()
            }
    }
}