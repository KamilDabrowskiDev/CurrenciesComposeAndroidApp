package com.currencies.compose.app.data.validator

import com.mbank.task.dabrowski.data.validator.TestContants
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Copyright(c) 2024 Kamil Dąbrowski. All rights reserved
 */

@RunWith(JUnitParamsRunner::class)
class CurrencyCodeValidatorTest {

    private val currencyCodeValidator = CurrencyCodeValidator()

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
    fun `validate, invalid code, should throw Exception`(code: String) {
        //  then
        currencyCodeValidator.validate(code)
    }

    @Test
    fun `validate, valid code, should not throw exception`() {
        //  then
        currencyCodeValidator.validate(TestContants.VALID_CURRENCY_CODE)
    }

}