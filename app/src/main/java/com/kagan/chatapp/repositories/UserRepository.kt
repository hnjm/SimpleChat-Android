package com.kagan.chatapp.repositories

import com.kagan.chatapp.api.UserApi
import com.kagan.chatapp.models.UserUpdateVM
import java.util.*

class UserRepository constructor(
    private val userApi: UserApi
) {
    fun getUsers() = userApi.getUsers()
    fun getUser(id: UUID) = userApi.getUser(id)
    fun updateUser(id: UUID, user: UserUpdateVM) = userApi.updateUser(id, user)
}