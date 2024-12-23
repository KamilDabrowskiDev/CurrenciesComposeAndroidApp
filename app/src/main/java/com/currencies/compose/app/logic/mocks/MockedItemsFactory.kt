package com.currencies.compose.app.logic.mocks

import com.currencies.compose.app.ui.screen.currencies.CurrencyItem
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.UUID
import kotlin.random.Random

/**
 * Copyright(c) 2024 Kamil Dąbrowski. All rights reserved
 */
class MockedItemsFactory {

    companion object {

        fun mockedCurrencyItems(): List<CurrencyItem> {
            return buildList {
                repeat(300) {
                    add(
                        CurrencyItem(
                            currencyTableName = "A",
                            currencyName = UUID.randomUUID().toString().take(5),
                            currencyCode = (1..3)
                                .map { ('A'..'Z').random() }
                                .joinToString(""),
                            averageExchangeRatePLN = generateRandomBigDecimal(
                                BigDecimal(0.1),
                                BigDecimal(5.5)
                            ).toDouble()
                        )
                    )
                }
            }
        }

        private fun generateRandomBigDecimal(from: BigDecimal, to: BigDecimal): BigDecimal {
            val range = to.subtract(from).toDouble()
            val randomValue = from.toDouble() + (Random.nextDouble() * range)
            return BigDecimal(randomValue).setScale(4, RoundingMode.HALF_UP)
        }

    }

}