package com.example.relaxation.ui.utils

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.relaxation.ui.activities.SingleChatActivity
import com.squareup.picasso.Picasso


class MessageAdapter : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    private var messageList = emptyList<String>()
    class MessageViewHolder(view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(com.example.relaxation.R.layout.item_chat_message_layout, parent, false)
        Log.d("Test", "Message VH created")
        return MessageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun onBindViewHolder(holder: MessageViewHolder, positionInRV: Int) {
        val messageCardCV: CardView = holder.itemView.findViewById(com.example.relaxation.R.id.messageCard)
        val messageTextTV: TextView = holder.itemView.findViewById(com.example.relaxation.R.id.message_text)
        val messageTimeTV: TextView = holder.itemView.findViewById(com.example.relaxation.R.id.message_time)
        Log.d("POS", "$position")
        REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_CHAT_LIST).child(
            position.toString()).child(
            CHAT_MESSAGE_LIST).child((positionInRV + 1).toString()).child(MESSAGE_TEXT).addListenerForSingleValueEvent(AppValueEventListener{
            messageTextTV.text = it.value.toString()
            Log.d("UID", "$it")
            Log.d("Test", "RV text aploaded")
        })
        REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_CHAT_LIST).child(
            position.toString()).child(
            CHAT_MESSAGE_LIST).child((positionInRV + 1).toString()).child(MESSAGE_TIME).addListenerForSingleValueEvent(AppValueEventListener{
            messageTimeTV.text = it.value.toString()
            Log.d("UID", "$it")
            Log.d("Test", "RV time aploaded")
        })
        REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_CHAT_LIST).child(
            position.toString()).child(
            CHAT_MESSAGE_LIST).child((positionInRV + 1).toString()).child(MESSAGE_SENDER_ID).addListenerForSingleValueEvent(AppValueEventListener{
            val newUID = it.value.toString()
            if(newUID == UID){
                val layoutParams = messageCardCV.layoutParams as RecyclerView.LayoutParams
                layoutParams.layoutDirection = Gravity.END
                messageCardCV.layoutParams = layoutParams
                messageCardCV.setCardBackgroundColor(Color.CYAN)
                Log.d("UID", "$it n $newUID")
                Log.d("Test", "RV color changed aploaded")
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setMessageList(List: List<String>){
        messageList = List
        notifyDataSetChanged()
    }
}