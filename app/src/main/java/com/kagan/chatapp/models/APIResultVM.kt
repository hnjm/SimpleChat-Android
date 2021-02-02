package com.kagan.chatapp.models

import java.util.*

data class APIResultVM(
    val recId: UUID? = null,
    val isSuccessful: Boolean,
    val errors: List<APIResultErrorCodeVM>? = null
)