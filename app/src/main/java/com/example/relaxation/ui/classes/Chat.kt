package com.example.relaxation.ui.classes

data class Chat(
    val chatID: String = "",
    val firstID: String = "",
    val secondID: String = "",
    val messageList: List<String> = listOf()
)