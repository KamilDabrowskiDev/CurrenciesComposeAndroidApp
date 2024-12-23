package com.currencies.compose.app.data.validator

import com.currencies.compose.app.data.error.InvalidApiFieldException
import com.currencies.compose.app.data.response.CurrentExchangeRateTableApiEntity


/**
 * Copyright(c) 2024 Kamil Dąbrowski. All rights reserved
 */

class CurrentExchangeRateTableApiEntityValidator(
    private val effectiveDateValidator: EffectiveDateValidator,
    private val entityValidator: CurrentExchangeRateApiEntityValidator
) {

    fun validate(entity: CurrentExchangeRateTableApiEntity) {

        if (entity.number.isBlank()) {
            throw InvalidApiFieldException("invalid NUMBER: ${entity.number}")
        }

        if (entity.table.isBlank()) {
            throw InvalidApiFieldException("invalid TABLE: ${entity.table}")
        }

        effectiveDateValidator.validate(entity.effectiveDate)

        entity.rates.forEach { rateEntity ->
            entityValidator.validate(rateEntity)
        }
    }
}