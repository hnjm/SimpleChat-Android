package com.kagan.chatapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.kagan.chatapp.R
import com.kagan.chatapp.adapters.MessageListAdapter
import com.kagan.chatapp.databinding.FragmentChatRoomBinding
import com.kagan.chatapp.db.entities.UsersEntity
import com.kagan.chatapp.models.*
import com.kagan.chatapp.models.chatrooms.MessageVM
import com.kagan.chatapp.models.chatrooms.OnReceivedMessageVM
import com.kagan.chatapp.utils.Constants.Companion.CHAT_HUB_URL
import com.kagan.chatapp.utils.States
import com.kagan.chatapp.utils.Utils
import com.kagan.chatapp.viewmodels.ChatRoomsViewModel
import com.kagan.chatapp.viewmodels.CurrentUserViewModel
import com.kagan.chatapp.viewmodels.DatabaseViewModel
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder
import com.microsoft.signalr.HubConnectionState
import dagger.hilt.android.AndroidEntryPoint
import io.sentry.Sentry
import javax.inject.Inject

@AndroidEntryPoint
class ChatRoomFragment : Fragment(R.layout.fragment_chat_room) {

    private lateinit var binding: FragmentChatRoomBinding
    private val safeArgs: ChatRoomFragmentArgs by navArgs()
    private val chatRoomViewModel: ChatRoomsViewModel by viewModels()
    private val userPreferenceViewModel: CurrentUserViewModel by viewModels()
    private val databaseViewModel: DatabaseViewModel by viewModels()
    private var auth = ""
    private var currentId = ""
    private lateinit var messageAdapter: MessageListAdapter
    private var messageList = arrayListOf<MessageVM>()
    private var userList = arrayListOf<UsersEntity>()
    private lateinit var hubConnection: HubConnection

    @Inject
    lateinit var gson: Gson

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChatRoomBinding.bind(view)

        init()
        subscribe()
    }

    private fun testSignalRConnection() {

        hubConnection.onClosed {
            Log.d("SignalR", "onClosed")
        }

        hubConnection.on(
            "OnConnect",
            { onConnect: String ->
                Log.d("SignalR", "OnConnect $onConnect")
                try {
                    val result = gson.fromJson(
                        onConnect,
                        OnConnectVM::class.java
                    )

                } catch (e: JsonSyntaxException) {
                    Sentry.captureException(e)
                }
            },
            String::class.java
        )

        hubConnection.on(
            "OnDisconnect", { message: String ->
                Log.d("SignalR", "OnDisconnect $message")
                try {
                    val result = gson.fromJson<OnDisconnectVM>(
                        message,
                        OnDisconnectVM::class.java
                    )

                } catch (e: JsonSyntaxException) {
                    Sentry.captureException(e)
                }
            }, String::class.java
        )
        hubConnection.on(
            "OnJoinToGroup", { message: String ->
                Log.d("SignalR", "OnJoinToGroup $message")
                var result: OnJoinToGroupVM? = null
                try {
                    result = gson.fromJson<OnJoinToGroupVM>(
                        message,
                        OnJoinToGroupVM::class.java
                    )
                } catch (e: JsonSyntaxException) {
                    Sentry.captureException(e)
                }
                result?.let {
                    showJoinedUser(it)
                }
            }, String::class.java
        )

        hubConnection.on(
            "ReceiveMessage", { message: String ->
                Log.d("SignalR", "ReceiveMessage $message")
                var result: OnReceivedMessageVM? = null
                try {
                    result = gson.fromJson<OnReceivedMessageVM>(
                        message,
                        OnReceivedMessageVM::class.java
                    )
                    result?.let { addToMessageList(it) }
                } catch (e: JsonSyntaxException) {
                    Sentry.captureException(e)
                }

            }, String::class.java
        )

        hubConnection.on(
            "ReceiveActiveUsers", { message: String ->
                Log.d("SignalR", "ReceiveActiveUsers $message")
                var result: ReceiveActiveUsersVM? = null
                try {
                    val activeUserType = object : TypeToken<ReceiveActiveUsersVM>() {}.type
                    result = gson.fromJson(
                        message,
                        activeUserType
                    )

                } catch (e: JsonSyntaxException) {
                    Sentry.captureException(e)
                }
            }, String::class.java
        )

        hubConnection.on(
            "ReceiveActiveUsersOfGroup", { message: String ->
                Log.d("SignalR", "ReceiveActiveUsersOfGroup $message")
                var result: ReceiveActiveUsersOfGroupVM? = null
                try {
                    result = gson.fromJson<ReceiveActiveUsersOfGroupVM>(
                        message,
                        ReceiveActiveUsersOfGroupVM::class.java
                    )
                } catch (e: JsonSyntaxException) {
                    Sentry.captureException(e)
                }
            }, String::class.java
        )

        hubConnection.on(
            "TestConnection", { message ->
                Log.d("SignalR", "TestConnection $message")

            }, String::class.java
        )

        hubConnection.on(
            "OnLeaveFromGroup", { message: String ->
                var result: OnLeaveFromGroupVM? = null
                try {
                    result = gson.fromJson<OnLeaveFromGroupVM>(
                        message,
                        OnLeaveFromGroupVM::class.java
                    )
                } catch (e: JsonSyntaxException) {
                    Sentry.captureException(e)
                }
            }, String::class.java
        )

        connectToSocket()
    }

    private fun addToMessageList(sender: OnReceivedMessageVM) {
        requireActivity().runOnUiThread {
            try {
                val testMessageVm = MessageVM(
                    sender.text,
                    safeArgs.chatRoomId.id,
                    Utils.getCurrentTime(),
                    null,
                    sender.senderId,
                    null
                )

                messageList.add(testMessageVm)
                binding.chatRoom.recyclerViewMessage.scrollToPosition(messageList.size - 1)
            } catch (e: NullPointerException) {
                Sentry.captureException(e)
            }
        }

    }

    private fun connectToSocket() {
        Log.d("SignalR", "Connect to Socket")
        //Connect to Server - Socket
        try {
            hubConnection.apply {
                start().blockingAwait()
                keepAliveInterval = 6000
            }
            if (hubConnection.connectionState == HubConnectionState.CONNECTED) {
                Log.d("SignalR", "connectToSocket: ")
                hubConnection.invoke("JoinToGroup", safeArgs.chatRoomId.id.toString())
                    .blockingAwait()
            }
        } catch (e: Exception) {
            Sentry.captureException(e)
        }
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
                    closeConnection()
                    true
                }
                R.id.search -> {
                    Log.d(TAG, "Search Button clicked")
                    true
                }
                else -> false
            }
        }

        binding.chatRoom.btnSend.setOnClickListener {
            signalRConnect()
        }

        binding.chatRoom.evMessage.addTextChangedListener {
            binding.chatRoom.btnSend.isEnabled = it?.isNotEmpty() == true
        }
    }

    private fun signalRConnect() {

        val message = binding.chatRoom.evMessage.text.toString()
        binding.chatRoom.evMessage.text.clear()

        signalRSend("SendMessage", message)
    }

    private fun signalRSend(method: String, message: String) {
        if (hubConnection.connectionState == HubConnectionState.CONNECTED) {
            message.let {
                hubConnection.send(method, message)
            }
        }
    }

    private fun showJoinedUser(data: OnJoinToGroupVM) {
        requireActivity().runOnUiThread {
            val user = userList.find {
                it.Id == data.userId.toString()
            }

            if (user != null) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.join_room, user.DisplayName),
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.cannot_find_user),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun subscribe() {
        Log.d(TAG, "subscribe: subscribe running")
        chatRoomViewModel.chatRoomMessage.observe(viewLifecycleOwner, { state ->
            when (state) {
                is States.Loading -> {
                    Log.d(TAG, "subscribe: loading")
                    displayProgressBar(true)
                    databaseViewModel.getUsers()
                }
                is States.Success -> {
                    messageList.clear()
                    messageList.addAll(state.data.sortedBy {
                        it.createDT
                    })
                    binding.chatRoom.recyclerViewMessage.scrollToPosition(messageList.size - 1)
                    messageAdapter.notifyDataSetChanged()

                    hubConnection =
                        HubConnectionBuilder.create("$CHAT_HUB_URL?userId=$currentId")
                            .withHeader("Authorization", "Bearer $auth")
                            .build()
                    testSignalRConnection()

                    displayProgressBar(false)
                }
                else -> displayProgressBar(true)
            }
        })

        userPreferenceViewModel.accessTokenStore.observe(viewLifecycleOwner, { accessToken ->
            Log.d(TAG, "subscribe: first time running userpreferences")
            accessToken?.let {
                auth = it

                chatRoomViewModel.chatRoomMessage.value?.let {
                    // I thing when navigate using menu items, it is not destroy your fragment.
                    return@observe
                }
                chatRoomViewModel.getChatRoomMessages(auth, safeArgs.chatRoomId.id)
            }
        })

        userPreferenceViewModel.idStore.observe(viewLifecycleOwner, { id ->
            id?.let {
                currentId = id
                messageAdapter =
                    MessageListAdapter(requireContext(), messageList, currentId, userList)
                binding.chatRoom.recyclerViewMessage.adapter = messageAdapter
            }
        })

        databaseViewModel.usersStates.observe(viewLifecycleOwner, { state ->
            when (state) {
                is States.Success -> {
                    userList.addAll(state.data)
                }
                else -> displayProgressBar(true)
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

    override fun onDestroy() {
        closeConnection()
        super.onDestroy()
    }

    private fun closeConnection() {
        hubConnection.invoke("RemoveFromGroup", safeArgs.chatRoomId.id)
        hubConnection.invoke("OnDisconnectedAsync", safeArgs.chatRoomId.id)
        hubConnection.stop()
    }
}