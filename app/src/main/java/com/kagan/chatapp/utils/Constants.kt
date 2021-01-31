package com.kagan.chatapp.utils

class Constants {

    companion object {
        private const val HOST = "http://192.168.0.104:5050"

        //        const val BASE_URL = "https://10.0.2.2:5060/api/"
        const val BASE_URL = "${HOST}/api/"
        const val CHAT_HUB_URL = "http://192.168.0.104:5050/chathub/"
    }
}