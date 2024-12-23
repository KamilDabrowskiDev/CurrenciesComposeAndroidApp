package com.currencies.compose.app.ui.screen.currencies.mvi

/**
 * Copyright(c) 2024 Kamil Dąbrowski. All rights reserved
 */
sealed class CurrenciesViewEffect {

    object ExitApp : CurrenciesViewEffect()

    data class OpenCurrencyDetail(
        val currencyTableName: String,
        val currencyName: String,
        val currencyCode: String,
        val currencyAverageExchangeRatePLN: Double
    ) : CurrenciesViewEffect()
}