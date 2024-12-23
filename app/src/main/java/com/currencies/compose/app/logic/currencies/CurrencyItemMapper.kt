package com.currencies.compose.app.logic.currencies

import com.currencies.compose.app.data.response.CurrentExchangeRateApiEntity
import com.currencies.compose.app.ui.screen.currencies.CurrencyItem

/**
 * Copyright(c) 2024 Kamil Dąbrowski. All rights reserved
 */

class CurrencyItemMapper {

    fun from(entity: CurrentExchangeRateApiEntity, tableName: String): CurrencyItem {

        if (tableName.isEmpty()) {
            throw IllegalArgumentException("tableName should not be EMPTY")
        }

        val item = CurrencyItem(
            currencyTableName = tableName,
            currencyName = entity.currency,
            currencyCode = entity.code,
            averageExchangeRatePLN = entity.mid
        )

        return item
    }
}