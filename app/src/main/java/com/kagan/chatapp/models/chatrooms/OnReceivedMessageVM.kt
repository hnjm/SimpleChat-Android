package com.kagan.chatapp.models.chatrooms

import java.util.*

data class OnReceivedMessageVM(
    val groupId: UUID,
    val senderId: UUID,
    val text: String
)