package com.kagan.chatapp.models

import java.util.*

data class APIResult(
    val recId: UUID? = null,
    val rec: User? = null,
    val isSuccessful: Boolean,
    val error: List<ApiError>? = null
)