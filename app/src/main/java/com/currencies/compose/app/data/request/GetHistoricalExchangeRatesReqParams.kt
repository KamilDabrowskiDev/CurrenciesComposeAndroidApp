package com.currencies.compose.app.data.request

/**
 * Copyright(c) 2024 Kamil Dąbrowski. All rights reserved
 */

data class GetHistoricalExchangeRatesReqParams(
    val tableName: String,
    val currencyCode: String,
    val startDate: String,
    val endDate: String
)