package com.kagan.chatapp.models

import java.util.*

data class UserAuthenticationVM(
    override val Id: UUID,
    val UserName: String,
    val LastLoginDateTime: Date? = null,
    val DisplayName: String,
    val About: String,
    val AccessToken: String,
    val RefreshToken: String
) : BaseVM()

