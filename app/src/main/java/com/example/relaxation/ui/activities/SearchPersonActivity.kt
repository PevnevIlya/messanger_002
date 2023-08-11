package com.example.relaxation.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
    private lateinit var binding : ActivitySearchPersonBinding
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
            REF_DATABASE_ROOT.child(NODE_USERNAMES).child(username).get()
                .addOnCompleteListener { task0 ->
                    if (task0.isSuccessful) {
                        val newUID =
                            REF_DATABASE_ROOT.child(NODE_USERNAMES).child(username).get()
                        Log.d("Test", "UID get")
                        REF_DATABASE_ROOT.child(NODE_USERS).child(newUID.toString())
                            .addListenerForSingleValueEvent(AppValueEventListener {
                                val newUSER = it.getValue(User::class.java) ?: User()
                                USER.UserList.add(newUSER)
                                REF_DATABASE_ROOT.child(NODE_USERS).child(UID)
                                    .child(CHILD_USER_LIST).setValue(newUID)
                                Log.d("Test", "Succes")
                            })
                    } else {
                        Log.d("Test", "Username not exists")
                    }
                }
        }
    }

    private fun initFunc(){
        initFirebase()
        initUser()
    }
}
