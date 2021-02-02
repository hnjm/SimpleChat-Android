package com.kagan.chatapp.models.chatrooms

data class ChatRoomUpdateVM(
    val description: String,
    val isMain: Boolean,
    val isOneToOneChat: Boolean,
    val isPrivate: Boolean,
    val name: String,
    val users: List<String>
)
