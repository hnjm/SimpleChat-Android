package com.kagan.chatapp.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kagan.chatapp.R
import com.kagan.chatapp.models.chatrooms.MessageVM

class ReceivedMessageHolder
constructor(view: View) : RecyclerView.ViewHolder(view) {
    private var textChatDateOther: TextView? = null
    private var imageChatProfileOther: ImageView? = null
    private var textChatUserOther: TextView? = null
    private var textChatMessageOther: TextView? = null
    private var textChatTimestampOther: TextView? = null

    init {
        textChatDateOther = itemView.findViewById(R.id.text_gchat_date_other)
        imageChatProfileOther = itemView.findViewById(R.id.image_gchat_profile_other)
        textChatUserOther = itemView.findViewById(R.id.text_gchat_user_other)
        textChatMessageOther = itemView.findViewById(R.id.text_gchat_message_other)
        textChatTimestampOther = itemView.findViewById(R.id.text_gchat_timestamp_other)
    }

    fun bind(received: MessageVM) {
        received.apply {
            textChatDateOther?.text = CreateDT
            textChatUserOther?.text = CreateBy.toString()
            textChatMessageOther?.text = Text
            textChatTimestampOther?.text = CreateDT
        }
    }
}