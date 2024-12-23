package com.currencies.compose.app.data.validator

import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Copyright(c) 2024 Kamil Dąbrowski. All rights reserved
 */

@RunWith(JUnitParamsRunner::class)
class EffectiveDateValidatorTest {

    private val validator = EffectiveDateValidator()

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
    fun `validate, invalid EFFECTIVE DATE, should throw Exception`(effectiveDate: String) {
        //  then
        validator.validate(effectiveDate)
    }

    @Test
    fun `validate, valid code, should not throw exception`() {
        //  when
        val validEffectiveDate = "2024-12-13"

        //  then
        validator.validate(validEffectiveDate)
    }

}