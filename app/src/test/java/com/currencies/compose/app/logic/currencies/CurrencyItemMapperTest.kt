package com.currencies.compose.app.logic.currencies

import com.currencies.compose.app.data.response.CurrentExchangeRateApiEntity
import junitparams.JUnitParamsRunner
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Copyright(c) 2024 Kamil Dąbrowski. All rights reserved
 */

@RunWith(JUnitParamsRunner::class)
class CurrencyItemMapperTest {

    private val mapper = CurrencyItemMapper()

    @Test
    fun `from, valid input, return proper item`() {

        //  when
        val testCurrency = "Dolar amerykański"
        val testCode = "USD"
        val testMid = 4.1
        val testTableName = "A"

        val entity = CurrentExchangeRateApiEntity(
            currency = testCurrency,
            code = testCode,
            mid = testMid
        )

        //  given
        val item = mapper.from(entity, testTableName)

        //  then
        Assert.assertTrue(item.currencyName == testCurrency)
        Assert.assertTrue(item.currencyCode == testCode)
        Assert.assertTrue(item.currencyTableName == testTableName)
        Assert.assertTrue(item.averageExchangeRatePLN == testMid)
    }

    @Test(expected = Exception::class)
    fun `from, invalid TABLE NAME, throwsException`() {

        //  when
        val testCurrency = "Dolar amerykański"
        val testCode = "USD"
        val testMid = 4.1
        val testTableName = ""

        val entity = CurrentExchangeRateApiEntity(
            currency = testCurrency,
            code = testCode,
            mid = testMid
        )

        //  then
        val item = mapper.from(entity, testTableName)
    }
}