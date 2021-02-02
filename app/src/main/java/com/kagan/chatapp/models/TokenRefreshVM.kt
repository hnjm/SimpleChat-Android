package com.kagan.chatapp.models

data class TokenRefreshVM(
    val accessToken: String,
    val refreshToken: String
)
