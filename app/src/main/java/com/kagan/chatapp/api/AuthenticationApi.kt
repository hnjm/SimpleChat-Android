package com.kagan.chatapp.api

import com.google.gson.JsonElement
import com.kagan.chatapp.models.RegisterUserVM
import com.kagan.chatapp.models.UserListVM
import com.kagan.chatapp.utils.Result
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.POST

interface AuthenticationApi {

    @POST("tokens/create")
    suspend fun login(
        @Field("UserName") username: String,
        @Field("Password") password: String
    ): Result<UserListVM>

    @POST("tokens/revoke")
    suspend fun logout()

    @POST("authentications/register")
    fun register(@Body registerUserVM: RegisterUserVM): Call<JsonElement>
}