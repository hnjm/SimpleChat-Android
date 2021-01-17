package com.kagan.chatapp.viewmodels

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.kagan.chatapp.models.UserUpdateVM
import com.kagan.chatapp.repositories.UserRepository
import com.kagan.chatapp.utils.UserEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class UserViewModel @ViewModelInject
constructor(
    private val userRepository: UserRepository,
    private val gson: Gson
) : ViewModel() {

    val isLoading = MutableLiveData<UserEvent>(UserEvent.Loading)

    fun getUsers() {

    }

    fun getUser(id: UUID) = viewModelScope.launch {
        Log.d(TAG, "getUser: running")
        delay(2000)

        isLoading.value = UserEvent.Success
    }

    fun updateUser(id: UUID, user: UserUpdateVM) {

    }

}