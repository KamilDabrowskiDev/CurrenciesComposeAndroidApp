package com.currencies.compose.app.data.validator

import com.currencies.compose.app.data.error.InvalidApiFieldException
import com.currencies.compose.app.data.response.CurrentExchangeRateApiEntity


/**
 * Copyright(c) 2024 Kamil Dąbrowski. All rights reserved
 */

class CurrentExchangeRateApiEntityValidator(
    private val currencyCodeValidator: CurrencyCodeValidator,
) {

    fun validate(entity: CurrentExchangeRateApiEntity) {

        if (entity.currency.isBlank()) {
            throw InvalidApiFieldException("INVALID currencyName is BLANK")
        }

        currencyCodeValidator.validate(entity.code)

        if (entity.mid <= 0) {
            throw InvalidApiFieldException("INVALID MID <= 0")
        }
    }
}