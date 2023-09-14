package com.example.relaxation.ui.utils

import android.media.browse.MediaBrowser.ItemCallback
import android.util.Log
import com.example.relaxation.ui.classes.Chat
import com.example.relaxation.ui.classes.Message
import com.example.relaxation.ui.classes.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.FieldPosition

lateinit var auth: FirebaseAuth
lateinit var REF_DATABASE_ROOT: DatabaseReference
lateinit var REF_STORAGE_ROOT: StorageReference
lateinit var USER:User
var companionPosition: Int = -2
var position: Int = -3
var CHAT: Chat = Chat()
var MESSAGE: Message = Message()
lateinit var UID:String

const val NODE_USERS = "Users"
const val NODE_USERNAMES = "Usernames"

const val FOLDER_PROFILE_IMAGE = "profile_image"

const val CHILD_ID = "id"
const val CHILD_EMAIL = "email"
const val CHILD_PASSWORD = "password"
const val CHILD_NAME = "name"
const val CHILD_STATE = "state"
const val CHILD_BIO = "bio"
const val CHILD_PHOTO_URL = "photoUrl"
const val CHILD_USER_LIST = "userList"
const val CHILD_CHAT_LIST = "chatList"
const val CHAT_ID = "chatID"
const val CHAT_IS_CREATED = "isCreated"
const val CHAT_MESSAGE_LIST = "messageList"
const val CHAT_SIZE = "size"
const val MESSAGE_ID = "messageID"
const val MESSAGE_SENDER_ID = "senderID"
const val MESSAGE_TEXT = "text"
const val MESSAGE_TIME = "time"

fun initFirebase(){
    auth = FirebaseAuth.getInstance()
    REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
    UID = auth.currentUser?.uid.toString()
    REF_STORAGE_ROOT = FirebaseStorage.getInstance().reference
}

fun initUser(callback: (user: User) -> Unit){
    Log.d("UID", "$UID")
    REF_DATABASE_ROOT.child(NODE_USERS).child(UID)
        .addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                USER = dataSnapshot.getValue(User::class.java) ?: User()
                callback(USER)
                Log.d("Test", "USER user initialized")
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("Test", "Ошибка при считывании User из Firebase: ${databaseError.message}")
            }
        })
}


