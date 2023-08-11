package com.example.relaxation.ui.utils

import android.util.Log

enum class AppStates(val state: String) {
    ONLINE("в сети"),
    OFFLINE("не в сети"),
    TYPING("печатает...");

    companion object{
        fun updateState(appStates: AppStates){
            REF_DATABASE_ROOT.child(NODE_USERS).child(UID)
                .child(CHILD_STATE).setValue(appStates.state)
                .addOnSuccessListener {
                    USER.state = appStates.state
                }
                .addOnFailureListener{
                    Log.d("Test", it.message.toString())
                }
        }
    }
}