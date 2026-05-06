package com.nohari.campus_hub.models

data class Announcement(
    var id: String = "",
    var title: String = "",
    var message: String = "",
    var timestamp: Long = System.currentTimeMillis()
)