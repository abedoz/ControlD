package com.controld.app.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FiltersBody(
    val filters: List<Filter> = emptyList()
)

@JsonClass(generateAdapter = true)
data class Filter(
    @Json(name = "PK") val pk: String = "",
    val name: String = "",
    val description: String? = null,
    val status: Int? = null,
    @Json(name = "sources_count") val sourcesCount: Int? = null,
    val group: String? = null
)
