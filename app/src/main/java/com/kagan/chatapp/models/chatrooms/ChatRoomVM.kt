package com.kagan.chatapp.models.chatrooms

import com.kagan.chatapp.models.BaseVM
import java.util.*

data class ChatRoomVM(
    override val id: UUID,
    val description: String,
    val isMain: Boolean,
    val isOneToOneChat: Boolean,
    val isPrivate: Boolean,
    val name: String,
    val users: List<String>
) : BaseVM()
