package com.currencies.compose.app.data.validator

import com.currencies.compose.app.data.response.CurrentExchangeRateApiEntity
import com.currencies.compose.app.data.response.CurrentExchangeRateTableApiEntity
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
class CurrentExchangeRateTableApiEntityValidatorTest {

    lateinit var validator: CurrentExchangeRateTableApiEntityValidator

    @Before
    fun before() {
        val currencyCodeValidator = CurrencyCodeValidator()
        val effectiveDateValidator = EffectiveDateValidator()
        val entityValidator = CurrentExchangeRateApiEntityValidator(currencyCodeValidator)

        validator = CurrentExchangeRateTableApiEntityValidator(
            effectiveDateValidator = effectiveDateValidator,
            entityValidator = entityValidator
        )
    }

    @Test(expected = Exception::class)
    @Parameters(
        "",
    )
    fun `validate, invalid TABLE, should throw Exception`(invalidTable: String) {

        //  when
        val entity = CurrentExchangeRateTableApiEntity(
            table = invalidTable,
            number = TestContants.VALID_NO,
            effectiveDate = TestContants.VALID_EFFECTIVE_DATE,
            rates = mockedValidCurrentExchangeRateApiEntities()
        )

        //  given
        validator.validate(entity)
    }


    @Test(expected = Exception::class)
    @Parameters(
        "",
    )
    fun `validate, invalid NO, should throw Exception`(invalidNo: String) {

        //  when
        val entity = CurrentExchangeRateTableApiEntity(
            table = TestContants.VALID_TABLE_NAME,
            number = invalidNo,
            effectiveDate = TestContants.VALID_EFFECTIVE_DATE,
            rates = mockedValidCurrentExchangeRateApiEntities()
        )

        //  given
        validator.validate(entity)
    }

    @Test(expected = Exception::class)
    @Parameters(
        "",
        "2024/12/13",     // Wrong delimiter
        "2024-12-32",     // Invalid day
        "2024-13-13",     // Invalid month
        "12-13-2024",     // Incorrect format
        "2024-1-1",       // Single digit day and month
        "-12-13",         // Missing year
        "2024-02-30",     // Invalid day in February
        "2023-02-29",     // Invalid date (not a leap year)
        "2024-13-01"      // Invalid month
    )
    fun `validate, invalid EFFECTIVE DATE, should throw Exception`(invalidEffectiveDate: String) {

        //  when
        val entity = CurrentExchangeRateTableApiEntity(
            table = TestContants.VALID_TABLE_NAME,
            number = TestContants.VALID_NO,
            effectiveDate = invalidEffectiveDate,
            rates = mockedValidCurrentExchangeRateApiEntities()
        )

        //  given
        validator.validate(entity)
    }

    /////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////

    private fun mockedValidCurrentExchangeRateApiEntities(): List<CurrentExchangeRateApiEntity> {
        return listOf(
            CurrentExchangeRateApiEntity(
                currency = "dolar",
                code = "USD",
                mid = 4.1234
            ),
            CurrentExchangeRateApiEntity(
                currency = "Euro",
                code = "EUR",
                mid = 4.5678
            ),
            CurrentExchangeRateApiEntity(
                currency = "Funt",
                code = "GBP",
                mid = 5.2345
            ),
            CurrentExchangeRateApiEntity(
                currency = "Frank",
                code = "CHF",
                mid = 4.2345
            ),
        )
    }

}