package com.kagan.chatapp.repositories

import com.kagan.chatapp.api.LoginApi
import com.kagan.chatapp.models.User

class LoginRepository(private val api: LoginApi) {

    suspend fun login(username: String, password: String) = api.login(username, password)
    fun logout() = api.logout()
    suspend fun register(user: User) = api.register(user)

}