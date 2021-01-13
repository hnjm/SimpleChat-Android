package com.kagan.chatapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.kagan.chatapp.models.*
import com.kagan.chatapp.repositories.LoginRepository
import com.kagan.chatapp.utils.Result
import io.sentry.Sentry
import io.sentry.SentryLevel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val TAG = "LoginViewModel"

class LoginViewModel(private val repository: LoginRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean> = _loginResult
    private val _registerResult = MutableLiveData<APIResultWithRecVM<UserAuthenticationVM>>()
    val registerResultWithRecVM: LiveData<APIResultWithRecVM<UserAuthenticationVM>> =
        _registerResult

    private val _registerErrors = MutableLiveData<APIResultVM>()
    val registerErrors: LiveData<APIResultVM> = _registerErrors

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

    fun register(registerUserVM: RegisterUserRequestVM) {
        val call = repository.register(registerUserVM.requestBody)

        call.enqueue(object : Callback<JsonElement> {
            override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                Log.d("retrofit", "call failed")
                Sentry.captureMessage(t.toString(), SentryLevel.ERROR)

                registerUserVM.requestCount++
                if (registerUserVM.requestCount < 3) {
                    register(registerUserVM)
                } else {
                    // todo Something happened
                }
            }

            override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {
                if (response == null) {
                    Sentry.captureMessage("$call response is null", SentryLevel.ERROR)
                } else {
                    val gson = GsonBuilder().create()
                    when (response.code()) {
                        400 -> {
                            try {
                                val errors = response.errorBody()?.string()
                                val model = gson.fromJson<APIResultVM>(
                                    errors,
                                    APIResultVM::class.java
                                )

                                _registerErrors.value = model

                            } catch (e: JsonSyntaxException) {
                                Sentry.captureMessage(e.toString(), SentryLevel.ERROR)
                            } catch (e: Exception) {
                                Sentry.captureMessage(e.toString(), SentryLevel.ERROR)
                            }
                        }
                        201 -> {
                            try {
                                val body = response.body()?.toString()
                                val rec = gson.fromJson<APIResultWithRecVM<UserAuthenticationVM>>(
                                    body.toString(),
                                    APIResultWithRecVM::class.java
                                )

                                Log.d(TAG, "body: $body")

                            } catch (e: JsonSyntaxException) {
                                Sentry.captureMessage(e.toString(), SentryLevel.ERROR)
                            } catch (e: JSONException) {
                                Sentry.captureMessage(e.toString(), SentryLevel.ERROR)
                            } catch (e: Exception) {
                                Sentry.captureMessage(e.toString(), SentryLevel.ERROR)
                            }
                        }
                        422 -> {
                            try {
                                val errors = response.errorBody()?.string()
                                val model = gson.fromJson<APIResultVM>(
                                    errors,
                                    APIResultVM::class.java
                                )

                                _registerErrors.value = model

                            } catch (e: JsonSyntaxException) {
                                Sentry.captureMessage(e.toString(), SentryLevel.ERROR)
                            } catch (e: Exception) {
                                Sentry.captureMessage(e.toString(), SentryLevel.ERROR)
                            }
                        }
                        500 -> {
                            // todo something happened
                        }
                    }
                }
            }
        })
    }
}