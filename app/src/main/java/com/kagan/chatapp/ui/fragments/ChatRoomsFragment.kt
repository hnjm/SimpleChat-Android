package com.kagan.chatapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kagan.chatapp.R
import com.kagan.chatapp.databinding.FragmentChatRoomsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatRoomsFragment : Fragment(R.layout.fragment_chat_rooms) {

    private lateinit var binding: FragmentChatRoomsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChatRoomsBinding.bind(view)
    }

    private fun displayProgressBar(b: Boolean) {
        if (b) {
            binding.chatRoomsProgressBar.visibility = View.VISIBLE
        } else {
            binding.chatRoomsProgressBar.visibility = View.GONE
        }
    }
}