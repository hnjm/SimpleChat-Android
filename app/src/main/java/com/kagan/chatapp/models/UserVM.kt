package com.kagan.chatapp.models

import java.util.*

data class UserVM(
    override val id: UUID,
    val userName: String,
    val isAccountLocked: Boolean,
    val displayName: String,
    val about: String
) : BaseVM()