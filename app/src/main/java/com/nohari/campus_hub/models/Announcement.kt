package com.nohari.campus_hub.models

data class Announcement(
    val id: String = "",
    val title: String = "",
    val message: String = "",
    val timestamp: String = "",
    val type: String = "announcement"
)