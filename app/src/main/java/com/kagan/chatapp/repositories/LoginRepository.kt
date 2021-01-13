package com.kagan.chatapp.repositories

import com.kagan.chatapp.api.AuthenticationApi
import com.kagan.chatapp.models.RegisterUserVM

class LoginRepository constructor(
    private val authenticationApi: AuthenticationApi
) {

    suspend fun login(username: String, password: String) =
        authenticationApi.login(username, password)

    suspend fun logout() = authenticationApi.logout()
    fun register(registerUserVM: RegisterUserVM) = authenticationApi.register(registerUserVM)
}