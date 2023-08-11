package com.example.relaxation.ui.utils

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.relaxation.databinding.FragmentChatsBinding
import com.squareup.picasso.Picasso


class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    lateinit var binding: FragmentChatsBinding
    private var userList = emptyList<String>()
    class UserViewHolder(binding: FragmentChatsBinding): RecyclerView.ViewHolder(binding.root)
    {
        init {
            binding.recyclerView.setOnClickListener{
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    OnItemClick(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        binding = FragmentChatsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
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

        Log.d("Test", userList[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(List: List<String>){
        userList = List
        notifyDataSetChanged()
    }
}