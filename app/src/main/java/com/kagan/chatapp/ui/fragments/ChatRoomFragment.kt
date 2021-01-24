package com.kagan.chatapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.kagan.chatapp.R
import com.kagan.chatapp.databinding.FragmentChatRoomBinding
import com.kagan.chatapp.utils.States
import com.kagan.chatapp.viewmodels.ChatRoomsViewModel
import com.kagan.chatapp.viewmodels.TokenPreferenceViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class ChatRoomFragment : Fragment(R.layout.fragment_chat_room) {

    private lateinit var binding: FragmentChatRoomBinding
    private val safeArgs: ChatRoomFragmentArgs by navArgs()
    private val chatRoomViewModel: ChatRoomsViewModel by viewModels()
    private val tokenPreferenceViewModel: TokenPreferenceViewModel by viewModels()
    private var auth = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChatRoomBinding.bind(view)

        init()
        subscribe()
    }

    private fun init() {
        binding.chatRoom.topAppBar.setNavigationOnClickListener {
            navigateUp()
        }

        binding.chatRoom.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.edit_room -> {
                    val action =
                        ChatRoomFragmentDirections.actionChatRoomFragmentToEditRoomFragment(safeArgs.chatRoomId)
                    navigate(action)
                    true
                }
                R.id.search -> {

                    true
                }
                else -> false
            }
        }
    }

    private fun subscribe() {
        chatRoomViewModel.chatRoomMessage.observe(viewLifecycleOwner, { state ->
            when (state) {
                is States.Loading -> {
                    displayProgressBar(true)
                }
                is States.Success -> {
                    displayProgressBar(false)

                }
                else -> displayProgressBar(false)
            }
        })

        tokenPreferenceViewModel.accessToken.observe(viewLifecycleOwner, { accessToken ->
            accessToken?.let {
                auth = it
                chatRoomViewModel.getChatRoomMessages(auth, safeArgs.chatRoomId.id)
            }
        })
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

    private fun navigate(action: NavDirections) {
        navController().navigate(action)
    }

    private fun navController() = findNavController()

    private fun navigateUp() {
        navController().navigateUp()
    }
}