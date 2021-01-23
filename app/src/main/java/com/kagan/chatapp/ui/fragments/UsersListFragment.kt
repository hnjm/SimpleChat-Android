package com.kagan.chatapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.kagan.chatapp.R
import com.kagan.chatapp.adapters.ChatRoomsAdapter
import com.kagan.chatapp.databinding.FragmentUsersListBinding
import com.kagan.chatapp.utils.OnClickListener
import java.util.*

class UsersListFragment : Fragment(R.layout.fragment_users_list), OnClickListener {

    private lateinit var usersAdapter: ChatRoomsAdapter
    private lateinit var binding: FragmentUsersListBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUsersListBinding.bind(view)

        init()
        subscribe()
    }

    private fun init() {
        binding.userList.topAppBar.title = getString(R.string.users)
        binding.userList.chatRoomRecyclerView.adapter = usersAdapter

    }

    private fun subscribe() {

    }


    override fun onclick(chatRoomId: UUID) {
        TODO("Not yet implemented")
    }

}