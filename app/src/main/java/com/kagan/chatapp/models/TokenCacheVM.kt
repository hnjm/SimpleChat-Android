package com.kagan.chatapp.models

import java.util.*

data class TokenCacheVM(
    val accessToken: String,
    val accessTokenExpiryTime: Date,
    val refreshToken: String,
    val refreshTokenExpiryTime: Date
)
