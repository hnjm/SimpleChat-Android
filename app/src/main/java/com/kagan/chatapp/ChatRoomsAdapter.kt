package com.kagan.chatapp

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.kagan.chatapp.models.chatrooms.AddVM

class ChatRoomsAdapter
constructor(private val itemList: ArrayList<AddVM>) :
    RecyclerView.Adapter<ChatRoomsAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardViewLayout: CardView = view.findViewById<CardView>(R.id.cvLayout)
        val ivUserPics: ImageView = view.findViewById(R.id.ivUserPics)
        val tvUserOrTitle: TextView = view.findViewById(R.id.tvUsernameOrTitle)
        val tvDesc: TextView = view.findViewById(R.id.tvDesc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.list_view, parent, false
        )

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            cardViewLayout.setOnClickListener {
                Toast.makeText(
                    holder.itemView.context, "Chat Room id:" +
                            "${itemList[position].Id}",
                    Toast.LENGTH_SHORT
                ).show()
            }
            ivUserPics.setImageResource(R.drawable.ic_baseline_android_24)
            tvUserOrTitle.text = itemList[position].Name
            tvDesc.text = itemList[position].Description
        }
    }

    override fun getItemCount() = itemList.size
}