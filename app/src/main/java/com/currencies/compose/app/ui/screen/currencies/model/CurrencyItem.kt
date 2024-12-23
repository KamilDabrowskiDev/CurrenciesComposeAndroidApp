package com.currencies.compose.app.ui.screen.currencies.model

import java.math.BigDecimal

/**
 * Copyright(c) 2024 Kamil Dąbrowski. All rights reserved
 */

data class CurrencyItem(
    val currencyTableName: String,
    val currencyName: String,
    val currencyCode: String,
    val averageExchangeRatePLN: Double
) {
    val averageExchangeRatePLNText: String = BigDecimal.valueOf(averageExchangeRatePLN).toString()

}