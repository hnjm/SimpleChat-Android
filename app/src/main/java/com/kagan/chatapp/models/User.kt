package com.kagan.chatapp.models

data class User(
    val userName: String,
    val lastLoginDateTime: String,
    val displayName: String,
    val about: String,
    val accessToken: String,
    val refreshToken: String,
    val id: String
)