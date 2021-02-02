package com.kagan.chatapp.models.chatrooms

import java.util.*

data class MessageVM(
    val text: String,
    val chatRoomId: UUID,
    val createDT: String,
    val updateDT: String? = null,
    val createBy: UUID,
    val updateBy: UUID? = null
)