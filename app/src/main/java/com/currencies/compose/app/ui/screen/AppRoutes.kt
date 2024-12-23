package com.currencies.compose.app.ui.screen

import kotlinx.serialization.Serializable

/**
 * Copyright(c) 2024 Kamil Dąbrowski. All rights reserved
 */

@Serializable
object CurrenciesRoute

@Serializable
data class CurrencyDetailRoute(
    val currencyTableName: String,
    val currencyName: String,
    val currencyCode: String,
    val currencyAverageExchangeRatePLN: Double
)