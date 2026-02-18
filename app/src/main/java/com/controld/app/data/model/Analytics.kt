package com.controld.app.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AnalyticsEndpointsBody(
    val endpoints: List<AnalyticsEndpoint> = emptyList()
)

@JsonClass(generateAdapter = true)
data class AnalyticsEndpoint(
    @Json(name = "PK") val pk: String = "",
    val name: String = "",
    val region: String? = null,
    val active: Boolean? = null
)

@JsonClass(generateAdapter = true)
data class IpBody(
    val ip: String = "",
    val datacenter: String? = null
)
