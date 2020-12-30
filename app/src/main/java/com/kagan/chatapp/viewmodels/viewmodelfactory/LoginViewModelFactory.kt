package com.kagan.chatapp.viewmodels.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kagan.chatapp.repositories.LoginRepository
import com.kagan.chatapp.viewmodels.LoginViewModel

@Suppress("UNCHECKED_CAST")
class LoginViewModelFactory(private val repository: LoginRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


