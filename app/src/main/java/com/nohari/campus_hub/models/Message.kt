package com.nohari.campus_hub.models

data class Message(
    var id: String = "",
    var senderId: String = "",
    var receiverId: String = "",
    var text: String = "",
    var timestamp: Long = System.currentTimeMillis()
)