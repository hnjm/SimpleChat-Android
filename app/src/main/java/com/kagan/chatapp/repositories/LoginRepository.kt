package com.kagan.chatapp.repositories

import com.kagan.chatapp.api.RetrofitInstance
import com.kagan.chatapp.models.RegisterUser

class LoginRepository() {

    suspend fun login(username: String, password: String) =
        RetrofitInstance.API.login(username, password)

    suspend fun logout() = RetrofitInstance.API.logout()
    suspend fun register(registerUser: RegisterUser) = RetrofitInstance.API.register(registerUser)
}