package com.example.relaxation.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.relaxation.databinding.ActivitySearchPersonBinding
import com.example.relaxation.ui.classes.User
import com.example.relaxation.ui.utils.AppValueEventListener
import com.example.relaxation.ui.utils.CHILD_USER_LIST
import com.example.relaxation.ui.utils.NODE_USERNAMES
import com.example.relaxation.ui.utils.NODE_USERS
import com.example.relaxation.ui.utils.REF_DATABASE_ROOT
import com.example.relaxation.ui.utils.UID
import com.example.relaxation.ui.utils.USER
import com.example.relaxation.ui.utils.initFirebase
import com.example.relaxation.ui.utils.initUser

class SearchPersonActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchPersonBinding
    private lateinit var CompanionUser: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchPersonBinding.inflate(layoutInflater)
        initFunc()
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        binding.searchButton.setOnClickListener {
            val username = binding.editText.text.toString()
            Log.d("Test", "Onclick")
            REF_DATABASE_ROOT.child(NODE_USERNAMES)
                .addListenerForSingleValueEvent(AppValueEventListener {task0 ->
                    if (task0.hasChild(username)) {
                        val newUID = REF_DATABASE_ROOT.child(NODE_USERNAMES).child(username).addListenerForSingleValueEvent(AppValueEventListener{
                            val value = it.getValue(String::class.java)
                            if (value != null) {
                                val newUID = value
                                REF_DATABASE_ROOT.child(NODE_USERS).child(newUID)
                                    .addListenerForSingleValueEvent(AppValueEventListener{task1 ->
                                        CompanionUser = task1.getValue(User::class.java) ?: User()
                                        Log.d("Test", "User initialized")
                                    })
                                Log.d("Test", newUID.toString())
                                Log.d("Test", "UID get")
                                REF_DATABASE_ROOT.child(NODE_USERS).child(newUID.toString())
                                    .addListenerForSingleValueEvent(AppValueEventListener {
                                        USER.userList += newUID.toString()
                                        REF_DATABASE_ROOT.child(NODE_USERS).child(UID)
                                            .child(CHILD_USER_LIST).setValue(USER.userList)
                                        CompanionUser.userList += UID.toString()
                                        REF_DATABASE_ROOT.child(NODE_USERS).child(newUID)
                                            .child(CHILD_USER_LIST).setValue(CompanionUser.userList)
                                        Log.d("Test", "Success")
                                    })
                            }
                        })
                    }
                })
        }
    }

    private fun initFunc() {
        initFirebase()
        initUser{ }
    }
}

