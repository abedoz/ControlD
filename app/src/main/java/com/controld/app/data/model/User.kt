package com.controld.app.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserBody(
    val user: User? = null
)

@JsonClass(generateAdapter = true)
data class User(
    @Json(name = "PK") val pk: String = "",
    val email: String? = null,
    val status: Int? = null,
    @Json(name = "two_fa") val twoFa: Int? = null
)
