package com.example.allsmokeme.chat

import com.google.firebase.Timestamp
import java.util.*

class ChatMessage {
    var userUid: String = ""
    var messageText: String = ""
    var messageUser: String = ""
    var messageUid: String = ""
    var messageTime: Timestamp? = null

    constructor(userUid: String, messageText: String, messageUser: String, messageUid: String, messageTime: Timestamp?) {
        this.userUid = userUid
        this.messageText = messageText
        this.messageUser = messageUser
        this.messageUid = messageUid
        this.messageTime = messageTime

        // Инициализировать текущее время messageTime = Timestamp.now()//Date().time
    }

    constructor() {}
}