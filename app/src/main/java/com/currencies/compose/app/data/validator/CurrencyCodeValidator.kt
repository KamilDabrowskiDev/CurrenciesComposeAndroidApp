package com.currencies.compose.app.data.validator

import com.currencies.compose.app.data.error.InvalidApiFieldException


/**
 * Copyright(c) 2024 Kamil Dąbrowski. All rights reserved
 */

class CurrencyCodeValidator {

    fun validate(currencyCode: String) {
        if (!isValidCurrencyCode(currencyCode)) {
            throw InvalidApiFieldException("CURRENCY CODE invalid: $currencyCode")
        }
    }

    private fun isValidCurrencyCode(code: String): Boolean {
        val regex = "^[A-Z]{3}$".toRegex()
        return regex.matches(code)
    }
}