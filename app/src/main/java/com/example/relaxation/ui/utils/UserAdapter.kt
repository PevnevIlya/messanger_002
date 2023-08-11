package com.example.relaxation.ui.utils

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.relaxation.ui.classes.User
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator


class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private var userList = emptyList<User>()
    class UserViewHolder(view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(com.example.relaxation.R.layout.item_user_layout, parent, false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val profileNameTextView: TextView = holder.itemView.findViewById(com.example.relaxation.R.id.item_profile_name)
        val profileStateTextView: TextView = holder.itemView.findViewById(com.example.relaxation.R.id.item_profile_state)
        val profileImage: ImageView = holder.itemView.findViewById(com.example.relaxation.R.id.item_profile_image)
        profileNameTextView.text = userList[position].name
        profileStateTextView.text  = userList[position].state
        Picasso.get().load(userList[position].photoUrl).into(profileImage)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(List: List<User>){
        userList = List
        notifyDataSetChanged()
    }
}