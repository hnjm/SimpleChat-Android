package com.kagan.chatapp.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.kagan.chatapp.repositories.ChatRoomRepository

class ChatRoomsViewModel
@ViewModelInject
constructor(private val chatRoomsRepository: ChatRoomRepository) : ViewModel() {
}