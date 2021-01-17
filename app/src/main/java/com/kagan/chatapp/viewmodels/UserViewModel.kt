package com.kagan.chatapp.viewmodels

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.kagan.chatapp.models.APIResultWithRecVM
import com.kagan.chatapp.models.UserAuthenticationVM
import com.kagan.chatapp.models.UserUpdateVM
import com.kagan.chatapp.models.UserVM
import com.kagan.chatapp.repositories.UserRepository
import com.kagan.chatapp.utils.UserEvent
import io.sentry.Sentry
import io.sentry.SentryLevel
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class UserViewModel @ViewModelInject
constructor(
    private val userRepository: UserRepository,
    private val gson: Gson
) : ViewModel() {

    private val _isLoading = MutableLiveData<UserEvent>()
    val isLoading: LiveData<UserEvent> = _isLoading

    private val _user = MutableLiveData<APIResultWithRecVM<UserVM>>()
    val user: LiveData<APIResultWithRecVM<UserVM>> = _user

    init {
        _isLoading.value = UserEvent.Loading
    }

    fun getUsers() {

    }

    fun getUser(currentUser: UserAuthenticationVM) {
        Log.d(TAG, "getUser: running")
        val call = userRepository.getUser(currentUser.Id, currentUser.TokenData.AccessToken)

        call.enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                when (response.code()) {
                    200 -> {
                        try {
                            val body = response.body()
                            val userResultType =
                                object : TypeToken<APIResultWithRecVM<UserVM>>() {}.type
                            val recUser = gson
                                .fromJson<APIResultWithRecVM<UserVM>>(
                                    gson.toJson(body),
                                    userResultType
                                )

                            _user.value = recUser
                            _isLoading.value = UserEvent.Success

                        } catch (e: JsonSyntaxException) {
                            Sentry.captureMessage(e.toString(), SentryLevel.ERROR)
                        } catch (
                            e: JSONException
                        ) {
                            Sentry.captureMessage(e.toString(), SentryLevel.ERROR)
                        } catch (e: Exception) {
                            Sentry.captureMessage(e.toString(), SentryLevel.ERROR)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    fun updateUser(id: UUID, user: UserUpdateVM) {

    }

}