package com.currencies.compose.app.logic.historicalexchangerate

import com.currencies.compose.app.data.response.HistoricalExchangeRateApiEntity
import com.mbank.task.dabrowski.data.validator.TestContants
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.math.BigDecimal

/**
 * Copyright(c) 2024 Kamil Dąbrowski. All rights reserved
 */

@RunWith(JUnitParamsRunner::class)
class HistoricalExchangeRateItemMapperTest {


    lateinit var mapper: HistoricalExchangeRateItemMapper

    @Before
    fun before() {
        val isoTimeConverter = ISO8601TimeTextToTimestampUTCConverter()
        mapper = HistoricalExchangeRateItemMapper(isoTimeConverter)
    }

    @Test
    @Parameters(
        value = [
            "0.0000051, 0.0000056",   // 9.80%
            "0.0000135, 0.0000143",   // 5.93%
            "0.0002345, 0.0002400",   // 2.34%
            "0.0012345, 0.0013000",   // 5.30%
            "0.0082345, 0.0085000",   // 3.21%
            "0.0172345, 0.0180000",   // 4.42%
            "0.0501234, 0.0510000",   // 1.74%
            "0.1234567, 0.1250000",   // 1.25%
            "0.2345678, 0.2390000",   // 1.89%
            "0.3456789, 0.3500000",   // 1.26%
            "0.4567890, 0.4600000",   // 0.69%
            "1.2345678, 1.3000000",   // 5.31%
            "2.3456789, 2.4000000",   // 2.32%
            "3.4567890, 3.5000000",   // 1.25%
            "4.5678901, 4.6000000",   // 0.69%
            "5.6789012, 5.7000000",   // 0.37%
            "6.7890123, 6.8000000",   // 0.16%
            "7.8901234, 7.9000000",   // 0.13%
            "8.9012345, 9.0000000",   // 1.11%
            "9.0123456, 9.1000000",   // 0.97%
            "10.1234567, 10.2000000", // 0.76%
            "11.2345678, 11.3000000", // 0.58%
            "12.3456789, 12.4000000", // 0.44%
            "13.4567890, 13.5000000", // 0.32%
            "14.5678901, 14.6000000", // 0.22%
            "15.6789012, 15.7000000", // 0.14%
            "16.7890123, 16.8000000", // 0.07%
            "17.8901234, 17.9000000", // 0.06%
            "18.9012345, 19.0000000"  // 0.52%
        ]
    )
    fun `from, currentExchangeRate not differ than 10 percent from historical, return proper Item`(
        currentExchangeRate: String,
        historicalExchangeRate: String,
    ) {

        println("CURRENT: $currentExchangeRate")
        println("HISTORICAL: $historicalExchangeRate")

        //  when
        val instant = Clock.System.now()
        val dateIso8601Text = instant.toLocalDateTime(TimeZone.UTC).date.toString()

        val apiEntity = HistoricalExchangeRateApiEntity(
            no = TestContants.VALID_NO,
            effectiveDate = dateIso8601Text,
            mid = historicalExchangeRate.toDouble()
        )

        //  given
        val item = mapper.from(
            apiEntity = apiEntity,
            currentExchangeRate = BigDecimal(currentExchangeRate)
        )

        //  then
        Assert.assertFalse(item.isRateDiffBiggerThanAverage)
    }


    @Test
    @Parameters(
        value = [
            "0.0000050, 0.0000057",   // 14.00%
            "0.0000123, 0.0000141",   // 14.63%
            "0.0001234, 0.0001410",   // 14.29%
            "0.0012345, 0.0014000",   // 13.42%
            "0.0101234, 0.0115000",   // 13.63%
            "0.0501234, 0.0575000",   // 14.71%
            "0.1234567, 0.1400000",   // 13.43%
            "0.2345678, 0.2700000",   // 14.94%
            "0.3456789, 0.3950000",   // 14.26%
            "0.4567890, 0.5200000",   // 13.87%
            "1.2345678, 1.4000000",   // 13.42%
            "2.3456789, 2.7000000",   // 14.99%
            "3.4567890, 3.9000000",   // 12.83%
            "4.5678901, 5.2000000",   // 13.87%
            "5.6789012, 6.5000000",   // 14.46%
            "6.7890123, 7.7000000",   // 13.48%
            "7.8901234, 9.0000000",   // 14.06%
            "8.9012345, 10.2000000",  // 14.64%
            "9.0123456, 10.3000000",  // 14.29%
            "10.1234567, 11.5000000", // 13.62%
            "11.2345678, 12.8000000", // 13.87%
            "12.3456789, 14.1000000", // 14.23%
            "13.4567890, 15.3000000", // 13.70%
            "15.6789012, 17.9000000", // 14.10%
            "16.7890123, 18.8000000", // 12.00%
            "17.8901234, 20.5000000", // 14.47%
            "18.9012345, 21.5000000",  // 13.72%
            "0.0000050, 0.000005525",   // 10.5%
            "0.0001234, 0.0001360",     // 10.5%
            "1.2345678, 1.3635195",     // 10.5%
            "5.6789012, 6.2705659",     // 10.5%
            "10.1234567, 11.1774200"    // 10.5%
        ]
    )
    fun `from, currentExchangeRate differ MORE than 10 percent from historical, return proper Item`(
        currentExchangeRate: String,
        historicalExchangeRate: String,
    ) {

        println("CURRENT: $currentExchangeRate")
        println("HISTORICAL: $historicalExchangeRate")

        //  when
        val instant = Clock.System.now()
        val dateIso8601Text = instant.toLocalDateTime(TimeZone.UTC).date.toString()

        val apiEntity = HistoricalExchangeRateApiEntity(
            no = TestContants.VALID_NO,
            effectiveDate = dateIso8601Text,
            mid = historicalExchangeRate.toDouble()
        )

        //  given
        val item = mapper.from(
            apiEntity = apiEntity,
            currentExchangeRate = BigDecimal(currentExchangeRate)
        )

        //  then
        Assert.assertTrue(item.isRateDiffBiggerThanAverage)
    }



    @Test
    @Parameters(
        value = [
            "0.0000050, 0.0000055",   // 10.00%
            "0.0000123, 0.0000135",   // 10.00%
            "0.0001234, 0.0001357",   // 10.00%
            "0.0101234, 0.0111357",   // 10.00%
            "0.0501234, 0.0551357",   // 10.00%
            "0.1234567, 0.1358023",   // 10.00%
            "0.3456789, 0.3802468",   // 10.00%
            "1.2345678, 1.3580246",   // 10.00%
            "2.3456789, 2.5802468",   // 10.00%
            "5.6789012, 6.2467913",   // 10.00%
            "7.8901234, 8.6791357",   // 10.00%
            "8.9012345, 9.7913580",   // 10.00%
            "9.0123456, 9.9135801",   // 10.00%
            "10.1234567, 11.1358023", // 10.00%
            "11.2345678, 12.3580246", // 10.00%
            "12.3456789, 13.5802468", // 10.00%
            "15.6789012, 17.2467913", // 10.00%
            "17.8901234, 19.6791357", // 10.00%
            "18.9012345, 20.7913580", // 10.00%
        ]
    )
    fun `from, currentExchangeRate differ exactly 10 percent from historical, return proper Item`(
        currentExchangeRate: String,
        historicalExchangeRate: String
    ) {

        println("CURRENT: $currentExchangeRate")
        println("HISTORICAL: $historicalExchangeRate")

        // when
        val instant = Clock.System.now()
        val dateIso8601Text = instant.toLocalDateTime(TimeZone.UTC).date.toString()

        val apiEntity = HistoricalExchangeRateApiEntity(
            no = TestContants.VALID_NO,
            effectiveDate = dateIso8601Text,
            mid = historicalExchangeRate.toDouble()
        )

        // given
        val item = mapper.from(
            apiEntity = apiEntity,
            currentExchangeRate = BigDecimal(currentExchangeRate)
        )

        // then
        Assert.assertFalse(item.isRateDiffBiggerThanAverage)
    }
}