package com.kagan.chatapp.models

import java.util.*

data class ReceiveActiveUsersOfGroupVM(
    val activeUsers: List<UUID>,
    val groupId: String
)
