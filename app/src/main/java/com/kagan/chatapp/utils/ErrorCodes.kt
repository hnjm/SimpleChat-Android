package com.kagan.chatapp.utils

object ErrorCodes {

    fun getDescription(errorCode: String): String {
        if (errorCode == "ERR03002") {
            return "UserName must be at least 5 characters"
        }
        return ""
    }
}