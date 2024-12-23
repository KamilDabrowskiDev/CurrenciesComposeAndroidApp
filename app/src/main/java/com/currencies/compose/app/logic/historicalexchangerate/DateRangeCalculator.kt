package com.currencies.compose.app.logic.historicalexchangerate

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime

/**
 * Copyright(c) 2024 Kamil Dąbrowski. All rights reserved
 */

class DateRangeCalculator {

    fun getDateRangeFromTimestamp(timestamp: Long, daysBefore: Int): DateRange {
        val instant = Instant.fromEpochMilliseconds(timestamp)
        val endDate = instant.toLocalDateTime(TimeZone.UTC).date
        val startDate = endDate.minus(value = daysBefore, unit = DateTimeUnit.DAY)
        return DateRange(startDate.toString(), endDate.toString())
    }
}

data class DateRange(
    val startDate: String,
    val endDate: String
)