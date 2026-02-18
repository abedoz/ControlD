package com.controld.app.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProfilesBody(
    val profiles: List<Profile> = emptyList()
)

@JsonClass(generateAdapter = true)
data class ProfileBody(
    val profile: Profile? = null
)

@JsonClass(generateAdapter = true)
data class Profile(
    @Json(name = "PK") val pk: String = "",
    val name: String = "",
    val updated: Long? = null,
    val stats: ProfileStats? = null
)

@JsonClass(generateAdapter = true)
data class ProfileStats(
    val filters: Int? = null,
    val services: Int? = null,
    val rules: Int? = null
)

@JsonClass(generateAdapter = true)
data class ProfileOptionsBody(
    val options: List<ProfileOption> = emptyList()
)

@JsonClass(generateAdapter = true)
data class ProfileOption(
    @Json(name = "PK") val pk: String = "",
    val name: String = "",
    val description: String? = null,
    val value: Any? = null,
    val enabled: Int? = null
)
