package com.currencies.compose.app.data

import com.currencies.compose.app.data.request.GetCurrentExchangeTableReqParams
import com.currencies.compose.app.data.request.GetHistoricalExchangeRatesReqParams
import com.currencies.compose.app.data.response.CurrentExchangeRateTableApiEntity
import com.currencies.compose.app.data.response.HistoricalExchangeRatesResponseApiEntity


/**
 * Copyright(c) 2024 Kamil Dąbrowski. All rights reserved
 */

interface NBPApiRepository {

    suspend fun getCurrentExchangeRateTable(
        reqParams: GetCurrentExchangeTableReqParams
    ): List<CurrentExchangeRateTableApiEntity>

    suspend fun getHistoricalCurrencyExchangeRates(
        reqParams: GetHistoricalExchangeRatesReqParams
    ): HistoricalExchangeRatesResponseApiEntity
}