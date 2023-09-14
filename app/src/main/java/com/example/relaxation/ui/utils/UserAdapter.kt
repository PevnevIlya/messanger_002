package com.example.relaxation.ui.utils

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.relaxation.ui.activities.SingleChatActivity
import com.squareup.picasso.Picasso


class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private var userList = emptyList<String>()
    class UserViewHolder(view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(com.example.relaxation.R.layout.item_user_layout, parent, false)
        Log.d("Test", "User VH created")
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val profileNameTextView: TextView = holder.itemView.findViewById(com.example.relaxation.R.id.item_profile_name)
        val profileStateTextView: TextView = holder.itemView.findViewById(com.example.relaxation.R.id.item_profile_state)
        val profileImage: ImageView = holder.itemView.findViewById(com.example.relaxation.R.id.item_profile_image)
        REF_DATABASE_ROOT.child(NODE_USERS).child(userList[position]).child(CHILD_NAME).addListenerForSingleValueEvent(AppValueEventListener{
            profileNameTextView.text = it.getValue().toString()
        })
        REF_DATABASE_ROOT.child(NODE_USERS).child(userList[position]).child(CHILD_STATE).addListenerForSingleValueEvent(AppValueEventListener{
            profileStateTextView.text = it.getValue().toString()
        })
        REF_DATABASE_ROOT.child(NODE_USERS).child(userList[position]).child(CHILD_PHOTO_URL).addListenerForSingleValueEvent(AppValueEventListener{
            Picasso.get().load(it.getValue().toString()).into(profileImage)
        })
        holder.itemView.setOnClickListener {
           APP_ACTIVITY.startActivity(Intent(APP_ACTIVITY, SingleChatActivity::class.java).putExtra("pos",position))
        }

        Log.d("Test", userList[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(List: List<String>){
        userList = List
        notifyDataSetChanged()
    }
}