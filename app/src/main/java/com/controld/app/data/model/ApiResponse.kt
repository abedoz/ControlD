package com.controld.app.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiResponse<T>(
    val body: T? = null,
    val success: Boolean = false,
    val message: String? = null
)
