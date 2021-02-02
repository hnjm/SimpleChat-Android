package com.kagan.chatapp.models

import java.util.*

data class SignalRConnection(
    val id: String,
    val userId: UUID,
    val groupId: UUID? = null
)