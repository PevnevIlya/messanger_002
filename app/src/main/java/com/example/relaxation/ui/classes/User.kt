package com.example.relaxation.ui.classes

data class User(
    val id: String = "",
    var email: String = "",
    var password: String = "",
    var name: String = "",
    var bio: String = "",
    var state: String = "",
    var photoUrl: String = "empty",
    var userList: List<String> = listOf()
)
