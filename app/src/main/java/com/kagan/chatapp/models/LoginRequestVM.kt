package com.kagan.chatapp.models

data class LoginRequestVM(
    val userName: String,
    val password: String
)

class LoginUserRequestVM {
    lateinit var requestBody: LoginRequestVM
    var requestCount: Int = 0
}