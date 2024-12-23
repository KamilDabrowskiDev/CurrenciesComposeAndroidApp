package com.currencies.compose.app.data.response

import com.currencies.compose.app.data.response.CurrentExchangeRateApiEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Copyright(c) 2024 Kamil Dąbrowski. All rights reserved
 */


@JsonClass(generateAdapter = true)
data class CurrentExchangeRateTableApiEntity(
    @Json(name = "table") val table: String,
    @Json(name = "no") val number: String,
    @Json(name = "effectiveDate") val effectiveDate: String,
    @Json(name = "rates") val rates: List<CurrentExchangeRateApiEntity>
)