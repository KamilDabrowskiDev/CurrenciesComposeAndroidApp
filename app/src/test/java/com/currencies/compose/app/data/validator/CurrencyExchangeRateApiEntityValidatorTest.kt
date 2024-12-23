package com.currencies.compose.app.data.validator

import com.currencies.compose.app.data.response.CurrentExchangeRateApiEntity
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
class CurrencyExchangeRateApiEntityValidatorTest {


    lateinit var entityValidator: CurrentExchangeRateApiEntityValidator

    @Before
    fun before() {
        val currencyCodeValidator = CurrencyCodeValidator()
        entityValidator = CurrentExchangeRateApiEntityValidator(
            currencyCodeValidator = currencyCodeValidator
        )
    }

    @Test(expected = Exception::class)
    @Parameters(
        "",
        "  ",
    )
    fun `validate, invalid CURRENCY, should throw Exception`(invalidCurrency: String) {

        //  when
        val entity = CurrentExchangeRateApiEntity(
            currency = invalidCurrency,
            code = TestContants.VALID_CURRENCY_CODE,
            mid = TestContants.VALID_MID
        )

        //  given
        entityValidator.validate(entity)
    }


    @Test(expected = Exception::class)
    @Parameters(
        "",
        "  ",
        "a",    // Invalid code, too short
        "abc",  // Invalid code, only low letters
        "A",    // Invalid code, too short
        "ABCD",  // Invalid code, too long
    )
    fun `validate, invalid CODE, should throw Exception`(invalidCode: String) {

        //  when
        val entity = CurrentExchangeRateApiEntity(
            currency = TestContants.VALID_CURRENCY_CODE,
            code = invalidCode,
            mid = TestContants.VALID_MID
        )

        //  given
        entityValidator.validate(entity)
    }


    @Test(expected = Exception::class)
    @Parameters(
        "-1.0",
        "0.0",
    )
    fun `validate, invalid MID, should throw Exception`(invalidMid: Double) {

        //  when
        val entity = CurrentExchangeRateApiEntity(
            currency = TestContants.VALID_CURRENCY_CODE,
            code = TestContants.VALID_CURRENCY_CODE,
            mid = invalidMid
        )

        //  given
        entityValidator.validate(entity)
    }

    @Test
    fun `validate, valid input, should NOT throw Exception`() {

        //  when
        val entity = CurrentExchangeRateApiEntity(
            currency = TestContants.VALID_CURRENCY_CODE,
            code = TestContants.VALID_CURRENCY_CODE,
            mid = TestContants.VALID_MID
        )

        //  given
        entityValidator.validate(entity)
    }
}