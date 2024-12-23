package com.currencies.compose.app.data.validator

import com.currencies.compose.app.data.error.InvalidApiFieldException
import kotlinx.datetime.LocalDate

/**
 * Copyright(c) 2024 Kamil Dąbrowski. All rights reserved
 */

class EffectiveDateValidator {

    fun validate(effectiveDate: String) {
        if (!isValidDateFormat(effectiveDate)) {
            throw InvalidApiFieldException("invalid DATE format: $effectiveDate should be: yyyy-MM-dd")
        }
    }

    private fun isValidDateFormat(input: String): Boolean {
        val regex = "^\\d{4}-\\d{2}-\\d{2}$".toRegex()

        if (!input.matches(regex)) {
            return false
        }

        return try {

            val date = LocalDate.parse(input)

            if (date.monthNumber == 2 && date.dayOfMonth == 29 && !isLeapYear(date.year)) {
                return false
            }

            true

        } catch (e: Exception) {
            false
        }
    }

    private fun isLeapYear(year: Int): Boolean {
        return (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0))
    }

}