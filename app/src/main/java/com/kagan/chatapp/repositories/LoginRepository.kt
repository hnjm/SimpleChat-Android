package com.kagan.chatapp.repositories

import com.kagan.chatapp.dao.LoginDAO
import com.kagan.chatapp.models.User

class LoginRepository(private val dao: LoginDAO) {

    fun login(username: String, password: String) = dao.login(username, password)
    fun logout() = dao.logout()
    fun register(user: User) = dao.register(user)

}