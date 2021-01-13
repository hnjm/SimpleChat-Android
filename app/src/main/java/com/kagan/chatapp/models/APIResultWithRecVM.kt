package com.kagan.chatapp.models

import java.util.*

data class APIResultWithRecVM<T>(
    val recId: UUID? = null,
    val rec: T? = null,
    val isSuccessful: Boolean,
    val errorCodeVM: List<APIResultErrorCodeVM>? = null
)