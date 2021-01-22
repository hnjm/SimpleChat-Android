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
import com.kagan.chatapp.models.APIResultVM
import com.kagan.chatapp.models.APIResultWithRecVM
import com.kagan.chatapp.models.chatrooms.AddVM
import com.kagan.chatapp.models.chatrooms.ChatRoomVM
import com.kagan.chatapp.repositories.ChatRoomRepository
import com.kagan.chatapp.utils.ChatRoomsState
import io.sentry.Sentry
import io.sentry.SentryLevel
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import java.util.*

class ChatRoomsViewModel
@ViewModelInject
constructor(
    private val chatRoomsRepository: ChatRoomRepository,
    private val gson: Gson
) : ViewModel() {

    private val _state = MutableLiveData<ChatRoomsState<*>>()
    val state: LiveData<ChatRoomsState<*>> = _state

    private val _postState = MutableLiveData<ChatRoomsState<*>>()
    val postState: LiveData<ChatRoomsState<*>> = _postState

    private val _chatRoomUsers = MutableLiveData<ChatRoomsState<*>>()
    val chatRoomUsers: LiveData<ChatRoomsState<*>> = _chatRoomUsers

    private val _chatRoom = MutableLiveData<ChatRoomsState<*>>()
    val chatRoom: LiveData<ChatRoomsState<*>> = _chatRoom

    fun getChatRooms(auth: String) {
        _state.value = ChatRoomsState.Loading
        val call = chatRoomsRepository.getChatRooms(auth)

        call.enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                when (response.code()) {
                    200 -> {
                        val chatRoomsType = object : TypeToken<List<ChatRoomVM>>() {}.type
                        _state.value = ChatRoomsState.Success(
                            parseJsonToVM<List<ChatRoomVM>>(
                                response.body().toString(),
                                chatRoomsType
                            )
                        )
                    }
                    400 -> {
                        _state.value = ChatRoomsState.Error(
                            parseJsonToVM(
                                response.errorBody()?.string()!!,
                                APIResultVM::class.java
                            )
                        )
                    }
                    404 -> {
                        _state.value = ChatRoomsState.Error(
                            parseJsonToVM(
                                response.errorBody()?.string()!!,
                                APIResultVM::class.java
                            )
                        )
                    }
                    500 -> {
                        Log.d(TAG, "onResponse: 500")
                        //todo something happened
                    }
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    fun postChatRooms(auth: String, chatRooms: AddVM) {
        _postState.value = ChatRoomsState.Loading

        val call = chatRoomsRepository.postChatRooms(auth, chatRooms)

        call.enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                when (response.code()) {
                    200 -> {
                        _postState.value = ChatRoomsState.Success(
                            parseJsonToVM(
                                response.body().toString(),
                                APIResultVM::class.java
                            )
                        )
                    }
                    400 -> {
                        _postState.value =
                            ChatRoomsState.Error(
                                parseJsonToVM(
                                    response.errorBody()?.string()!!,
                                    APIResultVM::class.java
                                )
                            )
                    }
                    404 -> {
                        _postState.value =
                            ChatRoomsState.Error(
                                parseJsonToVM(
                                    response.errorBody()?.string()!!,
                                    APIResultVM::class.java
                                )
                            )
                    }
                    500 -> {
                        TODO("Something happened")
                    }
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    fun getChatRoomUsers(auth: String, id: UUID) {
        _chatRoomUsers.value = ChatRoomsState.Loading

        val call = chatRoomsRepository.getChatRoomUsers(auth, id)

        call.enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                when (response.code()) {
                    200 -> {
                        _chatRoomUsers.value = ChatRoomsState.Success(
                            parseJsonToVM(
                                response.body().toString(),
                                APIResultWithRecVM::class.java
                            )
                            // todo i don't understand what type return
                        )
                    }
                    400 -> {
                        _chatRoomUsers.value = ChatRoomsState.Error(
                            parseJsonToVM(
                                response.errorBody()?.string()!!,
                                APIResultVM::class.java
                            )
                        )
                    }
                    404 -> {
                        _chatRoomUsers.value = ChatRoomsState.Error(
                            parseJsonToVM(
                                response.errorBody()?.string()!!,
                                APIResultVM::class.java
                            )
                        )
                    }
                    500 -> {
                        TODO("Something happened")
                    }
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    fun getChatRoom(auth: String, id: UUID) {
        _chatRoom.value = ChatRoomsState.Loading

        val call = chatRoomsRepository.getChatRoom(auth, id)

        call.enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                when (response.code()) {

                    //todo change
                    200 -> {
                        _chatRoom.value = ChatRoomsState.Success(
                            parseJsonToVM(
                                response.body().toString(),
                                APIResultWithRecVM::class.java
                            )
                            // todo i don't understand what type return
                        )
                    }
                    400 -> {
                        _chatRoom.value = ChatRoomsState.Error(
                            parseJsonToVM(
                                response.errorBody()?.string()!!,
                                APIResultVM::class.java
                            )
                        )
                    }
                    404 -> {
                        _chatRoomUsers.value = ChatRoomsState.Error(
                            parseJsonToVM(
                                response.errorBody()?.string()!!,
                                APIResultVM::class.java
                            )
                        )
                    }
                    500 -> {
                        TODO("Something happened")
                    }
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    fun putChatRoom(auth: String, id: UUID, chatRooms: AddVM) {}
    fun deleteChatRoom(auth: String, id: UUID) {}

    private fun <T : Any> parseJsonToVM(body: String, types: Type): T {
        var parse: T? = null
        try {
            parse = gson.fromJson(
                body,
                types
            )
        } catch (e: JsonSyntaxException) {
            Sentry.captureMessage(e.toString(), SentryLevel.ERROR)
        } catch (e: JSONException) {
            Sentry.captureMessage(e.toString(), SentryLevel.ERROR)
        }
        return parse!!
    }

    private fun <T : Any> parseJsonToVM(
        body: String,
        clazz: Class<T>
    ): T {
        var parse: T? = null
        try {
            parse = gson.fromJson(
                body,
                clazz
            )
        } catch (e: JsonSyntaxException) {
            Sentry.captureMessage(e.toString(), SentryLevel.ERROR)
        } catch (e: JSONException) {
            Sentry.captureMessage(e.toString(), SentryLevel.ERROR)
        }
        return parse!!
    }
}