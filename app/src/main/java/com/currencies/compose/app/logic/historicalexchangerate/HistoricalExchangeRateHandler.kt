package com.currencies.compose.app.logic.historicalexchangerate

import com.currencies.compose.app.logic.Result
import com.currencies.compose.app.ui.screen.currencydetail.model.HistoricalCurrencyExchangeRateItem
import java.math.BigDecimal

/**
 * Copyright(c) 2024 Kamil Dąbrowski. All rights reserved
 */

interface HistoricalExchangeRateHandler {

    suspend fun getHistoricalExchangeRates(
        currencyCode: String,
        currentAverageExchangeRate: BigDecimal,
        tableName: String,
        queryDateRange: DateRange
    ): Result<List<HistoricalCurrencyExchangeRateItem>>

}