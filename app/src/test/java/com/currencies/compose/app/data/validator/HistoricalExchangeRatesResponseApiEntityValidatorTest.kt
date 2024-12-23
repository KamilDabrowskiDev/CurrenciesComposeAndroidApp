package com.currencies.compose.app.data.validator

import com.currencies.compose.app.data.response.HistoricalExchangeRatesResponseApiEntity
import com.mbank.task.dabrowski.data.validator.TestContants
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Copyright(c) 2024 Kamil Dąbrowski. All rights reserved
 */

@RunWith(JUnitParamsRunner::class)
class HistoricalExchangeRatesResponseApiEntityValidatorTest {

    private lateinit var responseValidator: HistoricalExchangeRatesResponseApiEntityValidator

    @Before
    fun before() {

        val currencyCodeValidator = CurrencyCodeValidator()
        val effectiveDateValidator = EffectiveDateValidator()
        val entityValidator = HistoricalExchangeRateApiEntityValidator(effectiveDateValidator)

        responseValidator = HistoricalExchangeRatesResponseApiEntityValidator(
            currencyCodeValidator = currencyCodeValidator,
            historicalExchangeRateApiEntityValidator = entityValidator
        )
    }

    @Test
    fun `validate, valid response, should not throw Exception`() {

        //  when
        val response = HistoricalExchangeRatesResponseApiEntity(
            table = TestContants.VALID_TABLE_NAME,
            currency = TestContants.VALID_CURRENCY_NAME,
            code = TestContants.VALID_CURRENCY_CODE,
            rates = emptyList()
        )

        //  then
        responseValidator.validate(response)
    }


    @Test(expected = Exception::class)
    fun `validate, table A empty, throw Exception`() {

        //  when
        val response = HistoricalExchangeRatesResponseApiEntity(
            table = "",
            currency = TestContants.VALID_CURRENCY_NAME,
            code = TestContants.VALID_CURRENCY_CODE,
            rates = emptyList()
        )

        //  then
        responseValidator.validate(response)
    }

    @Test(expected = Exception::class)
    fun `validate, currency empty, throw Exception`() {

        //  when
        val response = HistoricalExchangeRatesResponseApiEntity(
            table = TestContants.VALID_TABLE_NAME,
            currency = "",
            code = TestContants.VALID_CURRENCY_CODE,
            rates = emptyList()
        )

        //  then
        responseValidator.validate(response)
    }

    @Test(expected = Exception::class)
    @Parameters(
        value = [
            "",     // Invalid code empty
            "a",    // Invalid code, too short
            "abc",  // Invalid code, only low letters
            "A",    // Invalid code, too short
            "ABCD",  // Invalid code, too long
        ]
    )
    fun `validate , currency code, throw Exception`(invalidCode: String) {
        // when
        val response = HistoricalExchangeRatesResponseApiEntity(
            table = TestContants.VALID_TABLE_NAME,
            currency = TestContants.VALID_CURRENCY_NAME,
            code = invalidCode,
            rates = emptyList()
        )

        //  then
        responseValidator.validate(response)
    }
}