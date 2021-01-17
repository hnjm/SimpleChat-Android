package com.kagan.chatapp.api

import com.google.gson.JsonElement
import com.kagan.chatapp.models.UserUpdateVM
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.PUT
import java.util.*

interface UserApi {

    @GET("users/")
    fun getUsers(): Call<JsonElement>

    @GET("users/{id}")
    fun getUser(@Path("id") id: UUID): Call<JsonElement>

    @PUT("users/{id}")
    fun updateUser(@Path("id") id: UUID, @Body user: UserUpdateVM): Call<JsonElement>
}