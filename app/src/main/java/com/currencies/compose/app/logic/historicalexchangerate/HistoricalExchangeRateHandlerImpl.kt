package com.currencies.compose.app.logic.historicalexchangerate

import com.currencies.compose.app.common.async.DispatcherProvider
import com.currencies.compose.app.common.utils.AppLogger
import com.currencies.compose.app.data.NBPApiRepository
import com.currencies.compose.app.data.request.GetHistoricalExchangeRatesReqParams
import com.currencies.compose.app.data.validator.HistoricalExchangeRatesResponseApiEntityValidator
import com.currencies.compose.app.logic.ErrorType
import com.currencies.compose.app.logic.Result
import com.currencies.compose.app.ui.screen.currencydetail.model.HistoricalCurrencyExchangeRateItem
import kotlinx.coroutines.withContext
import java.math.BigDecimal

/**
 * Copyright(c) 2024 Kamil Dąbrowski. All rights reserved
 */

class HistoricalExchangeRateHandlerImpl(
    private val apiRepository: NBPApiRepository,
    private val dispatcherProvider: DispatcherProvider,
    private val historicalExchangeRateItemMapper: HistoricalExchangeRateItemMapper,
    private val responseValidator: HistoricalExchangeRatesResponseApiEntityValidator,
) : HistoricalExchangeRateHandler {


    override suspend fun getHistoricalExchangeRates(
        currencyCode: String,
        currentAverageExchangeRate: BigDecimal,
        tableName: String,
        queryDateRange: DateRange
    ): Result<List<HistoricalCurrencyExchangeRateItem>> {
        return withContext(dispatcherProvider.io()) {

            try {

                val historicalRatesResponse = apiRepository.getHistoricalCurrencyExchangeRates(
                    reqParams = GetHistoricalExchangeRatesReqParams(
                        currencyCode = currencyCode,
                        tableName = tableName,
                        startDate = queryDateRange.startDate,
                        endDate = queryDateRange.endDate
                    )
                )

                responseValidator.validate(historicalRatesResponse)

                val items = historicalRatesResponse.rates.map {
                    historicalExchangeRateItemMapper.from(it, currentAverageExchangeRate)
                }

                val sortedItems = items.sortedByDescending { it.dateTimestamp }

                return@withContext Result.Success(sortedItems)
            } catch (exc: Exception) {
                AppLogger.errorLog("ERROR getting historical ex rates: $exc")
                return@withContext Result.Failure(errorType = ErrorType.NotSpecifiedError)
            }
        }
    }
}