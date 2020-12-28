package com.kagan.chatapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.kagan.chatapp.datastore.UserPreference
import kotlinx.coroutines.launch

class UserPreferenceViewModel(application: Application) : AndroidViewModel(application) {

    private val dataStore = UserPreference(application)

    val getUser = dataStore.userFlow.asLiveData()

    fun saveUser(user: String) = viewModelScope.launch {
        dataStore.saveUser(user)
    }
}