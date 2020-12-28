package com.kagan.chatapp.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import com.kagan.chatapp.models.User
import kotlinx.coroutines.flow.map

class UserPreference(context: Context) {
    private val dataStore: DataStore<Preferences> = context.createDataStore(name = "user")

    companion object {
        val USER = preferencesKey<User>("user")
    }

    val userFlow = dataStore.data.map { preferences ->
        preferences[USER]
    }

    suspend fun saveUser(user: User) {
        dataStore.edit { preferences ->
            preferences[USER] = user
        }
    }
}