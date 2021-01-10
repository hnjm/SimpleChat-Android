package com.kagan.chatapp.api

import com.kagan.chatapp.models.APIResult
import com.kagan.chatapp.models.LoggedInUser
import com.kagan.chatapp.models.RegisterUser
import com.kagan.chatapp.utils.Result
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.POST

interface AuthenticationApi {

    @POST("tokens/create")
    suspend fun login(
        @Field("UserName") username: String,
        @Field("Password") password: String
    ): Result<LoggedInUser>

    @POST("tokens/revoke")
    suspend fun logout()

    @POST("authentications/register")
    suspend fun register(@Body registerUser: RegisterUser): APIResult
}