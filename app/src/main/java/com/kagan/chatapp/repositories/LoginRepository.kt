package com.kagan.chatapp.repositories

import com.kagan.chatapp.api.RetrofitInstance
import com.kagan.chatapp.models.RegisterUserVM

class LoginRepository() {

    suspend fun login(username: String, password: String) =
        RetrofitInstance.API.login(username, password)

    suspend fun logout() = RetrofitInstance.API.logout()
    fun register(registerUserVM: RegisterUserVM) = RetrofitInstance.API.register(registerUserVM)
}