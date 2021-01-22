package com.kagan.chatapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.kagan.chatapp.R
import com.kagan.chatapp.databinding.FragmentChatRoomBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ChatRoomFragment : Fragment(R.layout.fragment_chat_room) {

    private lateinit var binding: FragmentChatRoomBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChatRoomBinding.bind(view)

        lifecycleScope.launch {
            init()
        }
    }

    private suspend fun init() {
        delay(1000)
        displayProgressBar(false)
        binding.chatRoom.topAppBar.setNavigationOnClickListener {
            navigateUp()
        }

        binding.chatRoom.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.edit_room -> {
                    navigate(R.id.action_chatRoomFragment_to_editRoomFragment)
                    true
                }
                R.id.search -> {

                    true
                }
                else -> false
            }
        }
    }


    private fun displayProgressBar(b: Boolean) {
        if (b) {
            binding.chatRoomProgressBar.visibility = View.VISIBLE
            binding.chatRoom.root.visibility = View.GONE
        } else {
            binding.chatRoomProgressBar.visibility = View.GONE
            binding.chatRoom.root.visibility = View.VISIBLE
        }
    }

    private fun navigate(action: Int) {
        navController().navigate(action)
    }

    private fun navController() = findNavController()

    private fun navigateUp() {
        navController().navigateUp()
    }
}