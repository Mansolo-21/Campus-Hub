package com.nohari.campus_hub.models

import com.nohari.campus_hub.models.Event

sealed class EventUiState {
    object Loading : EventUiState()
    data class Success(val events: List<Event>) : EventUiState()
    data class Error(val message: String) : EventUiState()
    object Empty : EventUiState()
}