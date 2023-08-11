package com.example.relaxation.ui.classes

import android.renderscript.Sampler
import java.security.Key

data class User(
    val id:String = "",
    var email:String = "",
    var password:String = "",
    var name: String = "",
    var bio: String = "",
    var state:String = "",
    var photoUrl:String = "empty",
    var userList: HashMap<Key, User> = hashMapOf()
)
