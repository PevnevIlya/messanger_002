package com.example.relaxation.ui.utils

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class AppValueEventListener(val onSucces:(DataSnapshot) -> Unit) : ValueEventListener{
    override fun onDataChange(snapshot: DataSnapshot) {
        onSucces(snapshot)
    }

    override fun onCancelled(error: DatabaseError) {

    }
}