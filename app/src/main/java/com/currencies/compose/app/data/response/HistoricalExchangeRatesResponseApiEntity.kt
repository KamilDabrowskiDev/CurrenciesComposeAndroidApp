package com.currencies.compose.app.data.response

import com.currencies.compose.app.data.response.HistoricalExchangeRateApiEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Copyright(c) 2024 Kamil Dąbrowski. All rights reserved
 */


@JsonClass(generateAdapter = true)
data class HistoricalExchangeRatesResponseApiEntity(
    @Json(name = "table") val table: String,
    @Json(name = "currency") val currency: String,
    @Json(name = "code") val code: String,
    @Json(name = "rates") val rates: List<HistoricalExchangeRateApiEntity>
)