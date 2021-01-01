package com.kagan.chatapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kagan.chatapp.datastore.UserPreference
import com.kagan.chatapp.models.User
import com.kagan.chatapp.repositories.LoginRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import com.kagan.chatapp.models.Result
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.withContext

const val TAG = "LoginViewModel"

class LoginViewModel(private val repository: LoginRepository) : ViewModel() {

    private val _loginResult = MediatorLiveData<Boolean>()
    val loginResult: LiveData<Boolean> = _loginResult
    private val _registerResult = MediatorLiveData<Boolean>()
    val registerResult: LiveData<Boolean> = _registerResult

    fun login(username: String, password: String) {
        viewModelScope.launch(IO) {
            val result = repository.login(username, password)
            Log.d(TAG, "login view model: $result")

            withContext(Main) {
                _loginResult.value = result is Result.Success
            }
        }
    }

    fun logout() = viewModelScope.launch(IO) {
        repository.logout()
    }

    fun register(user: User) = viewModelScope.launch(IO) {
        val result = repository.register(user)

        withContext(Main) {
            _registerResult.value = result is Result.Success
        }
    }

}