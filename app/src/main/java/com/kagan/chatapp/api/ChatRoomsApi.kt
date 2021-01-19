package com.kagan.chatapp.api

import com.google.gson.JsonElement
import com.kagan.chatapp.models.chatrooms.AddVM
import retrofit2.Call
import retrofit2.http.*
import java.util.*

interface ChatRoomsApi {

    @GET("ChatRooms/")
    fun getChatRooms(): Call<JsonElement>

    @POST("ChatRooms/")
    fun postChatRooms(@Body chatRoom: AddVM): Call<JsonElement>

    @GET("ChatRooms/{id}/users")
    fun getChatRoomUsers(@Path("id") id: UUID): Call<JsonElement>

    @GET("ChatRooms/{id}/")
    fun getChatRoom(@Path("id") id: UUID): Call<JsonElement>

    @PUT("ChatRooms/{id}/")
    fun putChatRoom(@Path("id") id: UUID, @Body chatRoom: AddVM): Call<JsonElement>

    @DELETE("ChatRooms/{id}/")
    fun deleteChatRoom(@Path("id") id: UUID): Call<JsonElement>
}