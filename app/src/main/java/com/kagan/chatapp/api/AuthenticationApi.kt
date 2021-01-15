package com.kagan.chatapp.api

import com.google.gson.JsonElement
import com.kagan.chatapp.models.LoginRequestVM
import com.kagan.chatapp.models.LoginUserRequestVM
import com.kagan.chatapp.models.RegisterUserVM
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.POST

interface AuthenticationApi {

    @POST("tokens/create")
    fun login(@Body loginRequestVM: LoginRequestVM): Call<JsonElement>

    @POST("tokens/revoke")
    suspend fun logout()

    @POST("authentications/register")
    fun register(@Body registerUserVM: RegisterUserVM): Call<JsonElement>
}