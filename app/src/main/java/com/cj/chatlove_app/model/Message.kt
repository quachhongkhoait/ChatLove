package com.cj.chatlove_app.model

data class Message(
    var sender: String = "",
    var receiver: String = "",
    var timestamp: Long = 0,
    var type: String = "",
    var message: String = ""
)