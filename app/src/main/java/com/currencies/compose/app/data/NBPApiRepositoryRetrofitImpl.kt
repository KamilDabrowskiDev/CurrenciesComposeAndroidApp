package com.currencies.compose.app.data

import com.currencies.compose.app.data.request.GetCurrentExchangeTableReqParams
import com.currencies.compose.app.data.request.GetHistoricalExchangeRatesReqParams
import com.currencies.compose.app.data.response.CurrentExchangeRateTableApiEntity
import com.currencies.compose.app.data.response.HistoricalExchangeRatesResponseApiEntity


/**
 * Copyright(c) 2024 Kamil Dąbrowski. All rights reserved
 */

class NBPApiRepositoryRetrofitImpl(
    private val retrofitService: NBPApiRetrofitService
) : NBPApiRepository {

    override suspend fun getCurrentExchangeRateTable(
        reqParams: GetCurrentExchangeTableReqParams
    ): List<CurrentExchangeRateTableApiEntity> {
        return retrofitService.getCurrentExchangeRateTable(reqParams.tableName)
    }

    override suspend fun getHistoricalCurrencyExchangeRates(
        reqParams: GetHistoricalExchangeRatesReqParams
    ): HistoricalExchangeRatesResponseApiEntity {
        return retrofitService.getHistoricalExchangeRates(
            tableName = reqParams.tableName,
            currencyCode = reqParams.currencyCode,
            startDate = reqParams.startDate,
            endDate = reqParams.endDate
        )
    }
}