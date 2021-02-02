package com.kagan.chatapp.models

import java.util.*

data class ReceiveActiveUsersVM(
    val activeUsers: List<SignalRConnection>,
)
