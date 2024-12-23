package com.currencies.compose.app.logic.historicalexchangerate

import junitparams.JUnitParamsRunner
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Copyright(c) 2024 Kamil Dąbrowski. All rights reserved
 */

@RunWith(JUnitParamsRunner::class)
class DateRangeCalculatorTest {


    private val calculator = DateRangeCalculator()


    @Test
    fun `getDateRangeFromTimestamp, valid timestamp, valid date range`() {
        // when
        val timestamp = 1734192516584L

        // given
        val dateRange = calculator.getDateRangeFromTimestamp(timestamp, 14)

        // then
        assertEquals("2024-11-30", dateRange.startDate)
        assertEquals("2024-12-14", dateRange.endDate)
    }

    @Test
    fun `getDateRangeFromTimestamp, timestamp at the beginning of a year, valid date range`() {
        // when
        val timestamp = 1672531200000 // January 1st, 2023 00:00:00 UTC

        // given
        val dateRange = calculator.getDateRangeFromTimestamp(timestamp, 14)

        // then
        assertEquals("2022-12-18", dateRange.startDate) // 14 days before Jan 1st, 2023
        assertEquals("2023-01-01", dateRange.endDate)   // Jan 1st, 2023
    }

    @Test
    fun `getDateRangeFromTimestamp, timestamp from February 29th on a leap year, valid date range`() {
        // when
        val timestamp = 951782400000 // February 29th, 2000

        // given
        val dateRange = calculator.getDateRangeFromTimestamp(timestamp, 14)

        // then
        assertEquals("2000-02-15", dateRange.startDate)
        assertEquals("2000-02-29", dateRange.endDate)
    }

    @Test
    fun `getDateRangeFromTimestamp, timestamp with current date, valid date range`() {
        // when
        val currentTimestamp = Clock.System.now().toEpochMilliseconds()

        // given
        val dateRange = calculator.getDateRangeFromTimestamp(currentTimestamp, 14)

        // then
        val instant = Instant.fromEpochMilliseconds(currentTimestamp)
        val currentDateISO = instant.toLocalDateTime(TimeZone.UTC).date.toString()
        val expectedStartDateISO = LocalDate.parse(currentDateISO).minus(value = 14, unit = DateTimeUnit.DAY).toString()

        assertEquals(expectedStartDateISO, dateRange.startDate)
        assertEquals(currentDateISO, dateRange.endDate)
    }
}