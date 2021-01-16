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
import io.sentry.Sentry
import io.sentry.SentryLevel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
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

    private val _loginResult = MutableLiveData<UserAuthenticationVM>()
    val loginResult: LiveData<UserAuthenticationVM> = _loginResult

    private val _loginFailure = MutableLiveData<Boolean>()
    val loginFailure: LiveData<Boolean> = _loginFailure

    private val _loginError = MutableLiveData<APIResultVM>()
    val loginErrors: LiveData<APIResultVM> = _loginError

    private val _registerResultWithRec = MutableLiveData<APIResultWithRecVM<UserAuthenticationVM>>()
    val registerResultWithRecVM: LiveData<APIResultWithRecVM<UserAuthenticationVM>> =
        _registerResultWithRec

    private val _registerErrors = MutableLiveData<APIResultVM>()
    val registerErrors: LiveData<APIResultVM> = _registerErrors

    private val _registerOnFailure = MutableLiveData<Boolean>()
    val registerOnFailure: LiveData<Boolean> = _registerOnFailure

    fun login(loginRequestVM: LoginUserRequestVM) {
        val call = repository.login(loginRequestVM.requestBody)

        call.enqueue(object : Callback<JsonElement> {
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                _loginFailure.value = true
                Log.d("retrofit", "call failed")
                Sentry.captureMessage(t.toString(), SentryLevel.ERROR)

                loginRequestVM.requestCount++
                if (loginRequestVM.requestCount < 3) {
                    login(loginRequestVM)
                } else {
                    // todo Something happened
                    _loginFailure.value = false
                }
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                when (response.code()) {
                    400 -> {
                        loginResponseFailed(response)
                    }
                    404 -> {
                        loginResponseFailed(response)
                    }
                    409 -> {
                        loginResponseFailed(response)
                    }
                    200 -> {
                        try {
                            val body = response.body()
                            val user = parseJsonToVM(body!!, UserAuthenticationVM::class.java)

                            _loginResult.value = user
                        } catch (e: Exception) {
                            Sentry.captureMessage(e.toString(), SentryLevel.ERROR)
                        }
                    }
                    500 -> {
                        // todo something happened
                    }
                }
            }
        })
    }

    fun logout(authorization: String) {
        val call = repository.logout(authorization)

        call.enqueue(object : Callback<JsonElement> {
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d(TAG, "onFailure: ")
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                when (response.code()) {
                    200 -> {
                    }
                    204 -> {
                    }
                    400 -> {
                    }
                    409 -> {
                    }
                    500 -> {
                        // todo something happened
                    }
                }
            }
        })
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
                            registerResponseFailed(response)
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
                            registerResponseFailed(response)
                        }
                        500 -> {
                            // todo something happened
                        }
                    }
                }
            }
        })
    }

    private fun loginResponseFailed(response: Response<JsonElement>) {
        _loginError.value = convertResponseToVM(response)
    }

    private fun registerResponseFailed(response: Response<JsonElement>) {
        _registerErrors.value = convertResponseToVM(response)
    }

    private fun convertResponseToVM(response: Response<JsonElement>): APIResultVM? {
        var convert: APIResultVM? = null
        try {
            val errors = response.errorBody()?.string()
            convert = gson.fromJson(
                errors,
                APIResultVM::class.java
            )
        } catch (e: JsonSyntaxException) {
            Sentry.captureMessage(e.toString(), SentryLevel.ERROR)
        } catch (e: JSONException) {
            Sentry.captureMessage(e.toString(), SentryLevel.ERROR)
        } catch (e: Exception) {
            Sentry.captureMessage(e.toString(), SentryLevel.ERROR)
        }
        return convert
    }

    private fun <T : Any> parseJsonToVM(body: JsonElement, clazz: Class<T>): T {
        var parse: T? = null
        try {
            parse = gson.fromJson(
                gson.toJson(body),
                clazz
            )
        } catch (e: JsonSyntaxException) {
            Sentry.captureMessage(e.toString(), SentryLevel.ERROR)
        } catch (e: JSONException) {
            Sentry.captureMessage(e.toString(), SentryLevel.ERROR)
        }
        return parse!!
    }

    fun clearResult() {
        _loginResult.value = null
    }

    fun clearFailure() {
        _loginFailure.value = null
    }

    fun clearError() {
        _loginError.value = null
    }
}