package com.kagan.chatapp.models

data class User(
    val id: Int,
    val username: String,
    val displayName: String,
    val email: String,
    val password: String
)