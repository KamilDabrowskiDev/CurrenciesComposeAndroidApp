package com.currencies.compose.app.data.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Copyright(c) 2024 Kamil Dąbrowski. All rights reserved
 */

@JsonClass(generateAdapter = true)
data class HistoricalExchangeRateApiEntity(
    @Json(name = "no") val no: String,
    @Json(name = "effectiveDate") val effectiveDate: String,
    @Json(name = "mid") val mid: Double
)