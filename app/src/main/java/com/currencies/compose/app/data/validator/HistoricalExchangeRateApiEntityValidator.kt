package com.currencies.compose.app.data.validator

import com.currencies.compose.app.data.error.InvalidApiEntityException
import com.currencies.compose.app.data.response.HistoricalExchangeRateApiEntity

/**
 * Copyright(c) 2024 Kamil Dąbrowski. All rights reserved
 */


class HistoricalExchangeRateApiEntityValidator(
    private val effectiveDateValidator: EffectiveDateValidator
) {

    fun validate(entity: HistoricalExchangeRateApiEntity) {

        if (entity.mid <= 0) {
            throw InvalidApiEntityException("MID is LESS or equal ZERO")
        }

        if (entity.no.isEmpty()) {
            throw InvalidApiEntityException("NO is EMPTY")
        }

        effectiveDateValidator.validate(entity.effectiveDate)
    }
}