package com.controld.app.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RulesBody(
    val rules: List<Rule> = emptyList(),
    val groups: List<RuleGroup>? = null
)

@JsonClass(generateAdapter = true)
data class RuleBody(
    val rules: List<Rule> = emptyList()
)

@JsonClass(generateAdapter = true)
data class Rule(
    @Json(name = "PK") val pk: String = "",
    val action: RuleAction? = null,
    val group: String? = null,
    val order: Int? = null
)

@JsonClass(generateAdapter = true)
data class RuleAction(
    val status: Int = 0,
    @Json(name = "do") val doValue: String? = null
)

@JsonClass(generateAdapter = true)
data class RuleGroup(
    @Json(name = "PK") val pk: String = "",
    val group: String = "",
    val count: Int? = null
)
