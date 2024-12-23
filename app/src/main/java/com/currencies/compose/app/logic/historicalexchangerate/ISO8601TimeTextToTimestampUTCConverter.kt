package com.currencies.compose.app.logic.historicalexchangerate

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant


/**
 * Copyright(c) 2024 Kamil Dąbrowski. All rights reserved
 */

class ISO8601TimeTextToTimestampUTCConverter {

    fun from(isoDateText: String): Long {
        val localDate = LocalDate.parse(isoDateText)
        val instant = localDate.atTime(0, 0).toInstant(TimeZone.UTC)
        return instant.toEpochMilliseconds()
    }
}