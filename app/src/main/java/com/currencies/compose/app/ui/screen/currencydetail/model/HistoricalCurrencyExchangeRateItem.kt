package com.currencies.compose.app.ui.screen.currencydetail.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

/**
 * Copyright(c) 2024 Kamil Dąbrowski. All rights reserved
 */

data class HistoricalCurrencyExchangeRateItem(
    val dateText: String,
    val dateTimestamp: Long,
    val exchangeRateText: String,
    val isRateDiffBiggerThanAverage: Boolean
) {

    val exchangeRateFontColor = if (isRateDiffBiggerThanAverage)
        Color.Red
    else
        Color.Black

    val exchangeRateFontWeight = if (isRateDiffBiggerThanAverage)
        FontWeight.Bold
    else
        FontWeight.Normal

}