package com.kagan.chatapp.models.chatrooms

import java.util.*

data class OnReceivedMessageVM(
    val GroupId: UUID,
    val SenderId: UUID,
    val Text: String
)