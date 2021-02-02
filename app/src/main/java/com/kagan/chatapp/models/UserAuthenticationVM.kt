package com.kagan.chatapp.models

import java.util.*

data class UserAuthenticationVM(
    override val id: UUID,
    val userName: String,
    val displayName: String,
    val about: String,
    val tokenData: TokenCacheVM
) : BaseVM()

