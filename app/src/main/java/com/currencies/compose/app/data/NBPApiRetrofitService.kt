package com.currencies.compose.app.data

import com.currencies.compose.app.data.response.CurrentExchangeRateTableApiEntity
import com.currencies.compose.app.data.response.HistoricalExchangeRatesResponseApiEntity
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Copyright(c) 2024 Kamil Dąbrowski. All rights reserved
 */

interface NBPApiRetrofitService {

    @GET("exchangerates/tables/{table}/")
    suspend fun getCurrentExchangeRateTable(
        @Path("table") tableName: String
    ): List<CurrentExchangeRateTableApiEntity>

    @GET("exchangerates/rates/{table}/{currencyCode}/{startDate}/{endDate}")
    suspend fun getHistoricalExchangeRates(
        @Path("table") tableName: String,
        @Path("currencyCode") currencyCode: String,
        @Path("startDate") startDate: String,
        @Path("endDate") endDate: String,
    ): HistoricalExchangeRatesResponseApiEntity
}