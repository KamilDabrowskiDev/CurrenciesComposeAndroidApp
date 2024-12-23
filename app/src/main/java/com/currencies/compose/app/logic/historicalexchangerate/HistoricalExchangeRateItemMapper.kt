package com.currencies.compose.app.logic.historicalexchangerate

import com.currencies.compose.app.data.response.HistoricalExchangeRateApiEntity
import com.currencies.compose.app.ui.screen.currencydetail.model.HistoricalCurrencyExchangeRateItem
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Copyright(c) 2024 Kamil Dąbrowski. All rights reserved
 */

class HistoricalExchangeRateItemMapper(
    private val isoTimeConverter: ISO8601TimeTextToTimestampUTCConverter
) {

    fun from(
        apiEntity: HistoricalExchangeRateApiEntity,
        currentExchangeRate: BigDecimal
    ): HistoricalCurrencyExchangeRateItem {

        val exchangeRateValue = BigDecimal.valueOf(apiEntity.mid)

        val item = HistoricalCurrencyExchangeRateItem(

            dateTimestamp = isoTimeConverter.from(apiEntity.effectiveDate),

            dateText = apiEntity.effectiveDate,

            exchangeRateText = exchangeRateValue.toString(),

            isRateDiffBiggerThanAverage = hasDifferenceExceedingThreshold(
                currentValue = currentExchangeRate,
                historicalValue = exchangeRateValue,
            )
        )

        return item
    }

    private fun hasDifferenceExceedingThreshold(
        currentValue: BigDecimal,
        historicalValue: BigDecimal,
        thresholdPercent: BigDecimal = BigDecimal(10)
    ): Boolean {

        val absDifference = currentValue.subtract(historicalValue).abs()

        val currentValueScale = currentValue.scale()
        val historicalValueScale = historicalValue.scale()

        val usedScale = maxOf(currentValueScale, historicalValueScale)
        val smallerValue = minOf(currentValue, historicalValue)

        val percentageDifference = absDifference.divide(
            smallerValue,
            usedScale,
            RoundingMode.HALF_UP
        ).multiply(BigDecimal(100))

        return percentageDifference > thresholdPercent
    }
}