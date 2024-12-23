package com.currencies.compose.app.data.validator

import com.currencies.compose.app.data.response.HistoricalExchangeRateApiEntity
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
class HistoricalExchangeRateApiEntityValidatorTest {

    lateinit var validator: HistoricalExchangeRateApiEntityValidator

    @Before
    fun before() {
        val effectiveDateValidator = EffectiveDateValidator()
        validator = HistoricalExchangeRateApiEntityValidator(effectiveDateValidator)
    }

    @Test
    fun `validate, valid entity, should not throw Exception`() {
        //  when
        val entity = HistoricalExchangeRateApiEntity(
            no = TestContants.VALID_NO,
            mid = 2.0,
            effectiveDate = TestContants.VALID_EFFECTIVE_DATE
        )

        validator.validate(entity)
    }

    @Test(expected = Exception::class)
    fun `validate, NO empty, should throw Exception`() {
        //  when
        val entity = HistoricalExchangeRateApiEntity(
            no = "",
            mid = 2.0,
            effectiveDate = TestContants.VALID_EFFECTIVE_DATE
        )

        validator.validate(entity)
    }

    @Test(expected = Exception::class)
    @Parameters(
        value = [
            "",       // Invalid MID empty
            "0.0",    // Invalid MID ZERO
            "-2.0",    // Invalid LESS than ZERO
        ]
    )
    fun `validate, invalid MID, should not throw Exception`(invalidMid: Double) {
        //  when
        val entity = HistoricalExchangeRateApiEntity(
            no = TestContants.VALID_NO,
            mid = invalidMid,
            effectiveDate = TestContants.VALID_EFFECTIVE_DATE
        )

        validator.validate(entity)
    }

    @Test(expected = Exception::class)
    @Parameters(
        value = [
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
        ]
    )
    fun `validate, invalid EFFECTIVE DATE, should throw Exception`(invalidDate: String) {
        //  when
        val entity = HistoricalExchangeRateApiEntity(
            no = TestContants.VALID_NO,
            mid = TestContants.VALID_MID,
            effectiveDate = invalidDate
        )

        //  then
        validator.validate(entity)
    }
}