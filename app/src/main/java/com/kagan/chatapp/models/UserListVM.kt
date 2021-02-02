package com.kagan.chatapp.models

import java.util.*

data class UserListVM(
    override val id: UUID,
    val userName: String,
    val displayName: String
) : BaseVM()