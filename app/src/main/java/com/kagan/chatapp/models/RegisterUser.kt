package com.kagan.chatapp.models

data class RegisterUser(
    val username: String,
    val password: String,
    val confirmPassword: String,
    val displayName: String,
    val email: String,
    val about: String? = null,
)
