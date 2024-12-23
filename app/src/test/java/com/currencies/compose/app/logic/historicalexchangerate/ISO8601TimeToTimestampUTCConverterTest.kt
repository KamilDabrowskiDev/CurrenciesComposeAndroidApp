package com.currencies.compose.app.logic.historicalexchangerate

import junit.framework.TestCase.assertEquals
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Copyright(c) 2024 Kamil Dąbrowski. All rights reserved
 */

@RunWith(JUnitParamsRunner::class)
class ISO8601TimeToTimestampUTCConverterTest {


    private val mapper = ISO8601TimeTextToTimestampUTCConverter()

    @Test
    @Parameters(
        value = [
            "1970-01-01, 0",                  // VALID - Unix epoch start
            "2024-11-20, 1732060800000",      // VALID - date in UTC
            "2000-02-29, 951782400000",       // VALID - Leap year date
        ]
    )
    fun `from, valid iso8601 date text , return correct timestamp`(
        isoDateText: String,
        expectedTimestamp: Long
    ) {

        // when
        val result = mapper.from(isoDateText)

        // then
        assertEquals(expectedTimestamp, result)
    }

    @Test(expected = Exception::class)
    @Parameters(
        value = [
            "",                    // INVALID - Empty
            "2024/11/20",          // INVALID - Wrong delimiter
            "20-11-2024",          // INVALID - Wrong format
            "2024-13-01",          // INVALID - Invalid month
            "2024-11-32",          // INVALID - Invalid day
            "2023-02-29"           // INVALID - Invalid leap day
        ]
    )
    fun `from, invalid input, throw exception`(invalidIsoDateText: String) {
        //  when
        mapper.from(invalidIsoDateText)
    }


}