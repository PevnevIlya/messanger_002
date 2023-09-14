package com.example.relaxation.ui.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.relaxation.databinding.ActivitySingleChatBinding
import com.example.relaxation.ui.classes.Chat
import com.example.relaxation.ui.classes.User
import com.example.relaxation.ui.utils.AppValueEventListener
import com.example.relaxation.ui.utils.CHAT
import com.example.relaxation.ui.utils.CHAT_ID
import com.example.relaxation.ui.utils.CHAT_IS_CREATED
import com.example.relaxation.ui.utils.CHAT_MESSAGE_LIST
import com.example.relaxation.ui.utils.CHAT_SIZE
import com.example.relaxation.ui.utils.CHILD_CHAT_LIST
import com.example.relaxation.ui.utils.CHILD_NAME
import com.example.relaxation.ui.utils.CHILD_PHOTO_URL
import com.example.relaxation.ui.utils.CHILD_STATE
import com.example.relaxation.ui.utils.MESSAGE_ID
import com.example.relaxation.ui.utils.MESSAGE_SENDER_ID
import com.example.relaxation.ui.utils.MESSAGE_TEXT
import com.example.relaxation.ui.utils.MESSAGE_TIME
import com.example.relaxation.ui.utils.MessageAdapter
import com.example.relaxation.ui.utils.NODE_USERS
import com.example.relaxation.ui.utils.REF_DATABASE_ROOT
import com.example.relaxation.ui.utils.UID
import com.example.relaxation.ui.utils.USER
import com.example.relaxation.ui.utils.companionPosition
import com.example.relaxation.ui.utils.initFirebase
import com.example.relaxation.ui.utils.position
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

class SingleChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySingleChatBinding
    private lateinit var companionID: String
    private lateinit var CompanionUser: User
    private lateinit var adapter: MessageAdapter
    private var list: List<String> = emptyList()
    lateinit var messageRV: RecyclerView
    private var CompanionChat: Chat = Chat()
    var messageList: DatabaseReference? = null
    var companionMessageList: DatabaseReference? = null

    @SuppressLint("NotifyDataSetChanged", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Test", "SingleChat on create")
        binding = ActivitySingleChatBinding.inflate(layoutInflater)
        messageRV = binding.chatRV
        adapter = MessageAdapter()
        messageRV.adapter = adapter
        init { CHAT ->
            Log.d("Test", "init()")
            Log.d("Test", "Starting filling of rv")
            Log.d("Test", "${CHAT.size.toInt()}")
            Log.d("Test", "end of filling of rv")
            list = emptyList()
            for (i in 0 until CHAT.size.toInt()) {
                list += (i + 1).toString()
            }
            adapter.setMessageList(list)
            Log.d("list", "$list onCreate")
            if(CHAT.size.toInt() > 10) {
                binding.chatRV.smoothScrollToPosition(list.size - 1)
            }
        }
        binding.btnSend.setOnClickListener {
            CHAT.size = (CHAT.size.toInt() + 1).toString()
            val messageID = CHAT.size
            val messageText = binding.inputTV.text.toString()
            val messageTime = SimpleDateFormat("HH:mm").format(Calendar.getInstance().time)
            REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_CHAT_LIST).child(position.toString()).child(CHAT_SIZE).setValue(CHAT.size)
            REF_DATABASE_ROOT.child(NODE_USERS).child(companionID).child(CHILD_CHAT_LIST).child(companionPosition.toString()).child(CHAT_SIZE).setValue(CHAT.size)
            messageList?.child(messageID)?.child(MESSAGE_SENDER_ID)?.setValue(UID)
            messageList?.child(messageID)?.child(MESSAGE_TEXT)?.setValue(messageText)
            messageList?.child(messageID)?.child(MESSAGE_TIME)?.setValue(messageTime)
            messageList?.child(messageID)?.child(MESSAGE_ID)?.setValue(messageID)
            companionMessageList?.child(messageID)?.child(MESSAGE_SENDER_ID)?.setValue(UID)
            companionMessageList?.child(messageID)?.child(MESSAGE_TEXT)?.setValue(messageText)
            companionMessageList?.child(messageID)?.child(MESSAGE_TIME)?.setValue(messageTime)
            companionMessageList?.child(messageID)?.child(MESSAGE_ID)?.setValue(messageID)
        }
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        giveList { list ->
            if(list.size > 10) {
                binding.chatRV.smoothScrollToPosition(list.size - 1)
            }
        }
    }

    private fun init(callback: (chat: Chat) -> Unit){
            Log.d("Test", "INIT FUNC START")
            initFirebase()
            position = intent.getIntExtra("pos", -1)
            companionID = USER.userList[position!!]
            Log.d("Test", position.toString())
            Log.d("Test", USER.userList[position!!])
            REF_DATABASE_ROOT.child(NODE_USERS).child(companionID)
                .addListenerForSingleValueEvent(object : ValueEventListener {

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        CompanionUser = dataSnapshot.getValue(User::class.java) ?: User()
                        (0 until CompanionUser.userList.size).forEach { i ->
                            if (CompanionUser.userList[i] == UID) {
                                companionPosition = i
                                CompanionChat.chatID = i.toString()
                                Log.d("Test", "cycle $i ${CompanionUser.userList[i]}")
                            }
                        }
                        Log.d("Test", "Companion user initialized , pos = $companionPosition")
                        loadCompanionInfo()
                        initMyChat{ CHAT->
                            callback(CHAT)
                        }
                        initMyCompanionChat()
                        messageList =
                            REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_CHAT_LIST)
                                .child(position.toString()).child(CHAT_MESSAGE_LIST)
                        companionMessageList =
                            REF_DATABASE_ROOT.child(NODE_USERS).child(companionID)
                                .child(CHILD_CHAT_LIST).child(CompanionChat.chatID)
                                .child(CHAT_MESSAGE_LIST)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.d(
                            "Test",
                            "Ошибка при считывании User из Firebase: ${databaseError.message}"
                        )
                    }
                })
    }

    private fun initMyChat(callback: (chat: Chat) -> Unit){
        CHAT.chatID = position.toString()
        REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_CHAT_LIST).child(position.toString())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    CHAT = dataSnapshot.getValue(Chat::class.java) ?: Chat()
                    callback(CHAT)
                    Log.d("Test", "Chat is successfully retrieved: $CHAT")
                    Log.d("Test", "Chat is successfully created: $CHAT")
                    if(!dataSnapshot.exists()) {
                        CHAT.isCreated = "true"
                        REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_CHAT_LIST)
                            .child(position.toString()).child(CHAT_ID).setValue(position.toString())
                        REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_CHAT_LIST)
                            .child(position.toString()).child(CHAT_IS_CREATED)
                            .setValue(CHAT.isCreated)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Обработать ошибку при считывании данных
                    Log.d("Test", "Ошибка при считывании Chat из Firebase: ${databaseError.message}")
                }
            })
    }

    private fun initMyCompanionChat(){
        REF_DATABASE_ROOT.child(NODE_USERS).child(companionID).child(CHILD_CHAT_LIST).child(companionPosition.toString())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    CompanionChat = dataSnapshot.getValue(Chat::class.java) ?: Chat()
                    if (CompanionChat.isCreated != "true") {
                        Log.d("Test", "Companion Chat is not created")
                        CompanionChat.isCreated = "true"
                        CompanionChat.size = "0"
                        REF_DATABASE_ROOT.child(NODE_USERS).child(companionID).child(CHILD_CHAT_LIST).child(companionPosition.toString()).child(CHAT_ID).setValue(companionPosition.toString())
                        REF_DATABASE_ROOT.child(NODE_USERS).child(companionID).child(CHILD_CHAT_LIST).child(companionPosition.toString()).child(CHAT_IS_CREATED).setValue(CompanionChat.isCreated)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Обработать ошибку при считывании данных
                    Log.d("Test", "Ошибка при считывании Chat из Firebase: ${databaseError.message}")
                }
            })
    }

    private fun giveList(callback: (list: List<String>) -> Unit)
    {
        REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_CHAT_LIST).child(position.toString()).child(
            CHAT_MESSAGE_LIST).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                checkDatabase { database ->
                    Log.d("Func", "CheckDatabase func")
                    REF_DATABASE_ROOT = database.reference
                    initMyChat { CHAT->
                        list = emptyList()
                        for (i in 0 until CHAT.size.toInt()) {
                            list += (i + 1).toString()
                        }
                        adapter.setMessageList(list)
                        callback(list)
                        Log.d("list", "$list onStart")
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

    private fun loadCompanionInfo(){
        if(position != -1){
            REF_DATABASE_ROOT.child(NODE_USERS).child(companionID).child(CHILD_NAME).addListenerForSingleValueEvent(
                AppValueEventListener{
                    binding.profileName.text = it.getValue().toString()
                })
            REF_DATABASE_ROOT.child(NODE_USERS).child(companionID).child(CHILD_STATE).addListenerForSingleValueEvent(
                AppValueEventListener{
                    binding.profileStatus.text = it.getValue().toString()
                })
            REF_DATABASE_ROOT.child(NODE_USERS).child(companionID).child(CHILD_PHOTO_URL).addListenerForSingleValueEvent(
                AppValueEventListener{
                    Picasso.get().load(it.getValue().toString()).into(binding.profileImage)
                })
        }
    }

    private fun checkDatabase(callback: (firebaseDatabase: FirebaseDatabase) -> Unit) {
        val database = FirebaseDatabase.getInstance()
        callback(database)
    }
}