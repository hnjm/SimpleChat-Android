package com.kagan.chatapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.kagan.chatapp.R
import com.kagan.chatapp.adapters.ChatRoomsAdapter
import com.kagan.chatapp.databinding.FragmentChatRoomsBinding
import com.kagan.chatapp.models.chatrooms.ChatRoomVM
import com.kagan.chatapp.models.safeargs.ChatRoomId
import com.kagan.chatapp.utils.ChatRoomsState
import com.kagan.chatapp.utils.OnClickListener
import com.kagan.chatapp.viewmodels.ChatRoomsViewModel
import com.kagan.chatapp.viewmodels.TokenPreferenceViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class ChatRoomsFragment : Fragment(R.layout.fragment_chat_rooms), OnClickListener {

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
        chatRoomsAdapter = ChatRoomsAdapter(chatRoomsList, this)
        binding.chatRooms.chatRoomRecyclerView.adapter = chatRoomsAdapter

        binding.chatRooms.fabCreateRoom.setOnClickListener {
            navigate(R.id.action_chatRoomsFragment_to_createRoomFragment)
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

    override fun onclick(chatRoomId: UUID) {
        // todo DI
        val id = ChatRoomId(chatRoomId)
        val action =
            ChatRoomsFragmentDirections.actionChatRoomsFragmentToChatRoomFragment(id)
        navigate(action)
    }

    private fun navController() = findNavController()

    private fun navigate(action: Int) {
        navController().navigate(action)
    }

    private fun navigate(action: NavDirections) {
        navController().navigate(action)
    }
}