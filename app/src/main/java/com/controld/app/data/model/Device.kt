package com.controld.app.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DevicesBody(
    val devices: List<Device> = emptyList()
)

@JsonClass(generateAdapter = true)
data class DeviceBody(
    @Json(name = "device") val device: Device? = null
)

@JsonClass(generateAdapter = true)
data class Device(
    @Json(name = "PK") val pk: String = "",
    val name: String = "",
    @Json(name = "device_id") val deviceId: String? = null,
    val profile: DeviceProfile? = null,
    val resolvers: Resolvers? = null,
    val status: Int? = null,
    val icon: String? = null,
    @Json(name = "ts") val timestamp: Long? = null,
    @Json(name = "learn_ip") val learnIp: Int? = null
)

@JsonClass(generateAdapter = true)
data class DeviceProfile(
    @Json(name = "PK") val pk: String = "",
    val name: String = ""
)

@JsonClass(generateAdapter = true)
data class Resolvers(
    val uid: String? = null,
    val doh: String? = null,
    val dot: String? = null
)
