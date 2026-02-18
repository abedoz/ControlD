package com.controld.app.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ServicesBody(
    val services: List<ServiceCategory> = emptyList()
)

@JsonClass(generateAdapter = true)
data class ServiceCategory(
    @Json(name = "PK") val pk: String = "",
    val name: String = "",
    val count: Int? = null,
    val services: List<Service> = emptyList()
)

@JsonClass(generateAdapter = true)
data class Service(
    @Json(name = "PK") val pk: String = "",
    val name: String = "",
    val category: String? = null,
    val action: ServiceAction? = null,
    @Json(name = "unlock_location") val unlockLocation: String? = null
)

@JsonClass(generateAdapter = true)
data class ServiceAction(
    val status: Int = 0,
    @Json(name = "do") val doValue: String? = null
)
