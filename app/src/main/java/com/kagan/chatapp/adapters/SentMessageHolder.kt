package com.kagan.chatapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kagan.chatapp.R
import com.kagan.chatapp.models.UserListVM
import com.kagan.chatapp.models.chatrooms.MessageVM

class SentMessageHolder
constructor(
    view: View
) : RecyclerView.ViewHolder(view) {
    private var textChatDateOther: TextView? = null
    private var textChatUserOther: TextView? = null
    private var textChatMessageOther: TextView? = null
    private var textChatTimestampOther: TextView? = null

    init {
        textChatDateOther = itemView.findViewById(R.id.text_gchat_date_me)
        textChatMessageOther = itemView.findViewById(R.id.text_gchat_message_me)
        textChatTimestampOther = itemView.findViewById(R.id.text_gchat_timestamp_me)
    }

    fun bind(sent: MessageVM) {
        sent.apply {
            textChatDateOther?.text = CreateDT
            textChatMessageOther?.text = Text
            textChatTimestampOther?.text = CreateDT
        }
    }
}