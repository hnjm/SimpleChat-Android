package com.kagan.chatapp.api

import com.google.gson.JsonElement
import com.kagan.chatapp.models.chatrooms.AddVM
import retrofit2.Call
import retrofit2.http.*
import java.util.*

interface ChatRoomsApi {

    @GET("ChatRooms/")
    fun getChatRooms(@Header("Authorization") auth: String,): Call<JsonElement>

    @POST("ChatRooms/")
    fun postChatRooms(
        @Header("Authorization") auth: String,
        @Body chatRoom: AddVM
    ): Call<JsonElement>

    @GET("ChatRooms/{id}/users")
    fun getChatRoomUsers(
        @Header("Authorization") auth: String,
        @Path("id") id: UUID
    ): Call<JsonElement>

    @GET("ChatRooms/{id}/")
    fun getChatRoom(
        @Header("Authorization") auth: String,
        @Path("id") id: UUID
    ): Call<JsonElement>

    @PUT("ChatRooms/{id}/")
    fun putChatRoom(
        @Header("Authorization") auth: String,
        @Path("id") id: UUID,
        @Body chatRoom: AddVM
    ): Call<JsonElement>

    @DELETE("ChatRooms/{id}/")
    fun deleteChatRoom(
        @Header("Authorization") auth: String,
        @Path("id") id: UUID
    ): Call<JsonElement>
}