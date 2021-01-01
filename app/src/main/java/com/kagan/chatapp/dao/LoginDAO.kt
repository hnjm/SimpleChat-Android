package com.kagan.chatapp.dao

import android.util.Log
import com.kagan.chatapp.models.LoggedInUser
import com.kagan.chatapp.models.Result
import com.kagan.chatapp.models.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOError
import java.io.IOException
import java.lang.NullPointerException
import kotlin.random.Random

const val TAG = "LoginDAO"

class LoginDAO {

    private val users = arrayListOf<User>(
        User(1, "Kagan41", "Kagan Kuscu", "kuscukagan@gmail.com", "1234567"),
        User(2, "Kagan4141", "kagan", "kusc", "123"),
        User(3, "Sevcan16", "Sevcan", "sevcan2119@gmail.com", "pass"),
        User(4, "admin", "admin", "admin@admin", "admin")
    )

    suspend fun login(username: String, password: String): Result<LoggedInUser> {
        var result: Result<LoggedInUser>? = null

        try {
            // todo delete after delay function
            delay(2000)
            users.forEach {
                if (it.username == username && it.password == password) {
                    Log.d(TAG, "login: Logged in $username, $password")
                    result = Result.Success(
                        LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            "$username $password"
                        )
                    )
                    return@forEach
                }
            }

            if (result == null) {
                throw NullPointerException()
            }

        } catch (e: Throwable) {
            result = Result.Error(IOException("Error logging in", e))
        }

        return result!!
    }

    fun logout() {}

    suspend fun register(user: User): Result<User> {
        var result: Result<User>? = null
        delay(2000)

        users.add(user)
        result = when (Random.nextInt(0, 2)) {
            0 -> Result.Success(user)
            1 -> Result.Error(IOException())
            else -> Result.Error(NullPointerException())
        }

        return result
    }
}