package com.kagan.chatapp.viewmodels

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val TAG = "LoginViewModel"

class LoginViewModel @ViewModelInject
constructor(
    private val repository: LoginRepository,
    private val gson: Gson
) : ViewModel() {

    private val _loginResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean> = _loginResult

    private val _registerResultWithRec = MutableLiveData<APIResultWithRecVM<UserAuthenticationVM>>()
    val registerResultWithRecVM: LiveData<APIResultWithRecVM<UserAuthenticationVM>> =
        _registerResultWithRec

    private val _registerErrors = MutableLiveData<APIResultVM>()
    val registerErrors: LiveData<APIResultVM> = _registerErrors

    private val _registerOnFailure = MutableLiveData<Boolean>()
    val registerOnFailure: LiveData<Boolean> = _registerOnFailure

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
                _registerOnFailure.value = true
                Log.d("retrofit", "call failed")
                Sentry.captureMessage(t.toString(), SentryLevel.ERROR)

                registerUserVM.requestCount++
                if (registerUserVM.requestCount < 3) {
                    register(registerUserVM)
                } else {
                    // todo Something happened
                    _registerOnFailure.value = false
                }
            }

            override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {
                if (response == null) {
                    Sentry.captureMessage("$call response is null", SentryLevel.ERROR)
                } else {
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
                                val body = response.body()
                                val apiResultWithRecVMType = object :
                                    TypeToken<APIResultWithRecVM<UserAuthenticationVM>>() {}.type
                                val rec = gson.fromJson<APIResultWithRecVM<UserAuthenticationVM>>(
                                    gson.toJson(body),
                                    apiResultWithRecVMType
                                )

                                _registerResultWithRec.value = rec

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