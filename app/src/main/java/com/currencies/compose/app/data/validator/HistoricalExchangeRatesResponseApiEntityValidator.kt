package com.currencies.compose.app.data.validator

import com.currencies.compose.app.data.error.InvalidApiResponseException
import com.currencies.compose.app.data.response.HistoricalExchangeRatesResponseApiEntity

/**
 * Copyright(c) 2024 Kamil Dąbrowski. All rights reserved
 */

class HistoricalExchangeRatesResponseApiEntityValidator(
    private val currencyCodeValidator: CurrencyCodeValidator,
    private val historicalExchangeRateApiEntityValidator: HistoricalExchangeRateApiEntityValidator
) {

    fun validate(responseApiEntity: HistoricalExchangeRatesResponseApiEntity) {

        if (responseApiEntity.table.isEmpty()) {
            throw InvalidApiResponseException("tableName is: EMPTY")
        }

        if (responseApiEntity.currency.isEmpty()) {
            throw InvalidApiResponseException("currency is: EMPTY")
        }

        currencyCodeValidator.validate(responseApiEntity.code)

        responseApiEntity.rates.forEach { entity ->
            historicalExchangeRateApiEntityValidator.validate(entity)
        }
    }
}