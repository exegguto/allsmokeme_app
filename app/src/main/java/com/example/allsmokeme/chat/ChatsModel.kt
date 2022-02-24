package com.example.allsmokeme.chat

import com.google.firebase.Timestamp
import java.util.*

class ChatsModel {
    var lastMessage: String = ""
    var nameUser: String = ""
    var chatID: String = ""
    var nameChat: String = ""
    var timestamp: Timestamp? = null

    constructor(lastMessage: String, nameUser: String, chatID: String, nameChat: String, timestamp: Timestamp?) {
        this.lastMessage = lastMessage
        this.nameUser = nameUser
        this.nameChat = nameChat
        this.chatID = chatID
        this.timestamp = timestamp

        // Инициализировать текущее время timestamp = Timestamp.now()//Date().time
    }

    constructor() {}
}