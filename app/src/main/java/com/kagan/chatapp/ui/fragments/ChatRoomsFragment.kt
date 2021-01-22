package com.kagan.chatapp.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kagan.chatapp.ChatRoomsAdapter
import com.kagan.chatapp.R
import com.kagan.chatapp.databinding.FragmentChatRoomsBinding
import com.kagan.chatapp.models.chatrooms.AddVM
import com.kagan.chatapp.models.chatrooms.ChatRoomVM
import com.kagan.chatapp.utils.ChatRoomsState
import com.kagan.chatapp.viewmodels.ChatRoomsViewModel
import com.kagan.chatapp.viewmodels.TokenPreferenceViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChatRoomsFragment : Fragment(R.layout.fragment_chat_rooms) {

    private lateinit var binding: FragmentChatRoomsBinding
    private val chatRoomsViewModel: ChatRoomsViewModel by viewModels()
    private val tokenPreferenceViewModel: TokenPreferenceViewModel by viewModels()

    //    @Inject
    private lateinit var chatRoomsAdapter: ChatRoomsAdapter
    private var chatRoomsList = arrayListOf<ChatRoomVM>()
    private var accessToken = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChatRoomsBinding.bind(view)
        init()
        subscribe()
    }

    private fun init() {
        // todo will learn how to pass args with dagger2
        chatRoomsAdapter = ChatRoomsAdapter(chatRoomsList)
        binding.chatRooms.chatRoomRecyclerView.adapter = chatRoomsAdapter

        binding.chatRooms.fabCreateRoom.setOnClickListener {
            findNavController().navigate(R.id.action_chatRoomsFragment_to_createRoomFragment)
        }
    }

    private fun subscribe() {
        chatRoomsViewModel.state.observe(viewLifecycleOwner, { state ->
            when (state) {
                is ChatRoomsState.Loading -> {
                    displayProgressBar(true)
                }
                is ChatRoomsState.Success -> {
                    displayProgressBar(false)
                    chatRoomsList.addAll(state.data as List<ChatRoomVM>)
                    chatRoomsAdapter.notifyDataSetChanged()
                }
                is ChatRoomsState.Error -> {
                    displayProgressBar(false)
                    Log.d(TAG, "Error data:${state.error}")
                }
            }
        })

        tokenPreferenceViewModel.accessToken.observe(viewLifecycleOwner, {
            it?.let {
                accessToken = it
                chatRoomsViewModel.getChatRooms(accessToken)
            }
        })
    }

    private fun displayProgressBar(b: Boolean) {
        if (b) {
            binding.chatRoomsProgressBar.visibility = View.VISIBLE
            binding.chatRooms.root.visibility = View.GONE
        } else {
            binding.chatRoomsProgressBar.visibility = View.GONE
            binding.chatRooms.root.visibility = View.VISIBLE
        }
    }
}