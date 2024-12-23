package com.currencies.compose.app.logic.historicalexchangerate

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.currencies.compose.app.common.async.DispatcherProvider
import com.currencies.compose.app.data.NBPApiRepository
import com.currencies.compose.app.data.request.GetHistoricalExchangeRatesReqParams
import com.currencies.compose.app.data.response.HistoricalExchangeRateApiEntity
import com.currencies.compose.app.data.response.HistoricalExchangeRatesResponseApiEntity
import com.currencies.compose.app.data.validator.HistoricalExchangeRatesResponseApiEntityValidator
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.math.BigDecimal
import javax.inject.Inject

import com.currencies.compose.app.logic.Result


/**
 * Copyright(c) 2024 Kamil Dąbrowski. All rights reserved
 */


@HiltAndroidTest
@Config(application = HiltTestApplication::class)
@RunWith(AndroidJUnit4::class)
class HistoricalExchangeHandlerTest {


    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    private val mockedNBPApiRepository: NBPApiRepository = mockk<NBPApiRepository>()

    @Inject
    lateinit var dispatcherProvider: DispatcherProvider

    @Inject
    lateinit var itemMapper: HistoricalExchangeRateItemMapper

    @Inject
    lateinit var responseValidator: HistoricalExchangeRatesResponseApiEntityValidator

    @Inject
    lateinit var dateRangeCalculator: DateRangeCalculator

    @Inject
    lateinit var converter: ISO8601TimeTextToTimestampUTCConverter

    lateinit var handler: HistoricalExchangeRateHandler


    @Before
    fun before() {
        hiltRule.inject()

        handler = HistoricalExchangeRateHandlerImpl(
            apiRepository = mockedNBPApiRepository,
            dispatcherProvider = dispatcherProvider,
            historicalExchangeRateItemMapper = itemMapper,
            responseValidator = responseValidator
        )
    }

    @Test
    fun `getHistoricalExchangeRates, loadFromApi FAILED , return ResultFailure`() {
        runBlocking {

            //  when
            val testTableName = "A"
            val testCurrencyCode = "USD"
            val testCurrencyValueText = "4.14"

            val testQueryDateRange = dateRangeCalculator.getDateRangeFromTimestamp(
                timestamp = Clock.System.now().toEpochMilliseconds(),
                daysBefore = 14
            )

            coEvery {
                mockedNBPApiRepository.getHistoricalCurrencyExchangeRates(
                    GetHistoricalExchangeRatesReqParams(
                        tableName = testTableName,
                        currencyCode = testCurrencyCode,
                        startDate = testQueryDateRange.startDate,
                        endDate = testQueryDateRange.endDate
                    )
                )
            } throws RuntimeException()


            //  given
            val result = handler.getHistoricalExchangeRates(
                currencyCode = testCurrencyCode,
                currentAverageExchangeRate = BigDecimal(testCurrencyValueText),
                tableName = testTableName,
                queryDateRange = testQueryDateRange
            )

            //  then
            Assert.assertTrue(result is Result.Failure)
        }
    }


    @Test
    fun `getHistoricalExchangeRates, loadFromApi OK, return ResultSuccess`() {
        runBlocking {
            //  when
            val tableName = "A"
            val currencyName = "dolar amerykański"
            val currencyCode = "USD"
            val currentCurrencyAverageValueText = "4.14"

            val firstRateNo = "233/A/NBP/2024"
            val firstRateDate = "2024-12-02"
            val firstRateMid = 4.0827

            val secondRateNo = "234/A/NBP/2024"
            val secondRateDate = "2024-12-03"
            val secondRateMid = 4.0803

            val thirdRateNo = "235/A/NBP/2024"
            val thirdRateDate = "2024-12-04"
            val thirdRateMid = 4.0845


            val queryDateRange = dateRangeCalculator.getDateRangeFromTimestamp(
                timestamp = Clock.System.now().toEpochMilliseconds(),
                daysBefore = 14
            )

            val testResponse = HistoricalExchangeRatesResponseApiEntity(
                table = tableName,
                currency = currencyName,
                code = currencyCode,
                rates = listOf(

                    HistoricalExchangeRateApiEntity(
                        no = firstRateNo,
                        effectiveDate = firstRateDate,
                        mid = firstRateMid
                    ),
                    HistoricalExchangeRateApiEntity(
                        no = secondRateNo,
                        effectiveDate = secondRateDate,
                        mid = secondRateMid
                    ),
                    HistoricalExchangeRateApiEntity(
                        no = thirdRateNo,
                        effectiveDate = thirdRateDate,
                        mid = thirdRateMid
                    ),
                )
            )

            coEvery {
                mockedNBPApiRepository.getHistoricalCurrencyExchangeRates(
                    GetHistoricalExchangeRatesReqParams(
                        tableName = tableName,
                        currencyCode = currencyCode,
                        startDate = queryDateRange.startDate,
                        endDate = queryDateRange.endDate
                    )
                )
            } returns testResponse

            //  given
            val result = handler.getHistoricalExchangeRates(
                currencyCode = currencyCode,
                currentAverageExchangeRate = BigDecimal(currentCurrencyAverageValueText),
                tableName = tableName,
                queryDateRange = queryDateRange
            )

            //  then
            Assert.assertTrue(result is Result.Success)
            val items = (result as Result.Success).data
            Assert.assertTrue(items.size == 3)

        }
    }


    @Test
    fun `getHistoricalExchangeRates, loadFromApi OK, return VALID sorted Items`() {
        runBlocking {

            //  when
            val tableName = "A"
            val currencyName = "dolar amerykański"
            val currencyCode = "USD"
            val currentCurrencyAverageValueText = "4.09"

            val firstRateNo = "233/A/NBP/2024"
            val firstRateDate = "2024-12-02"
            val firstRateMid = 4.0827

            val secondRateNo = "234/A/NBP/2024"
            val secondRateDate = "2024-12-03"
            val secondRateMid = 4.0803

            val thirdRateNo = "235/A/NBP/2024"
            val thirdRateDate = "2024-12-04"
            val thirdRateMid = 4.0845

            val queryDateRange = dateRangeCalculator.getDateRangeFromTimestamp(
                timestamp = Clock.System.now().toEpochMilliseconds(),
                daysBefore = 14
            )

            val testResponse = HistoricalExchangeRatesResponseApiEntity(
                table = tableName,
                currency = currencyName,
                code = currencyCode,
                rates = listOf(

                    HistoricalExchangeRateApiEntity(
                        no = firstRateNo,
                        effectiveDate = firstRateDate,
                        mid = firstRateMid
                    ),
                    HistoricalExchangeRateApiEntity(
                        no = secondRateNo,
                        effectiveDate = secondRateDate,
                        mid = secondRateMid
                    ),
                    HistoricalExchangeRateApiEntity(
                        no = thirdRateNo,
                        effectiveDate = thirdRateDate,
                        mid = thirdRateMid
                    ),
                )
            )

            coEvery {
                mockedNBPApiRepository.getHistoricalCurrencyExchangeRates(
                    GetHistoricalExchangeRatesReqParams(
                        tableName = tableName,
                        currencyCode = currencyCode,
                        startDate = queryDateRange.startDate,
                        endDate = queryDateRange.endDate
                    )
                )
            } returns testResponse

            //  given
            val result = handler.getHistoricalExchangeRates(
                currencyCode = currencyCode,
                currentAverageExchangeRate = BigDecimal(currentCurrencyAverageValueText),
                tableName = tableName,
                queryDateRange = queryDateRange
            )

            //  then
            Assert.assertTrue(result is Result.Success)
            val items = (result as Result.Success).data
            Assert.assertTrue(items.size == 3)

            val firstItem = items[0]
            val secondItem = items[1]
            val thirdItem = items[2]

            Assert.assertTrue(firstItem.dateText == thirdRateDate)
            Assert.assertTrue(firstItem.dateTimestamp == converter.from(thirdRateDate))
            Assert.assertTrue(firstItem.exchangeRateText == BigDecimal.valueOf(thirdRateMid).toString())
            Assert.assertFalse(firstItem.isRateDiffBiggerThanAverage)

            Assert.assertTrue(secondItem.dateText == secondRateDate)
            Assert.assertTrue(secondItem.dateTimestamp == converter.from(secondRateDate))
            Assert.assertTrue(secondItem.exchangeRateText == BigDecimal.valueOf(secondRateMid).toString())
            Assert.assertFalse(secondItem.isRateDiffBiggerThanAverage)

            Assert.assertTrue(thirdItem.dateText == firstRateDate)
            Assert.assertTrue(thirdItem.dateTimestamp == converter.from(firstRateDate))
            Assert.assertTrue(thirdItem.exchangeRateText == BigDecimal.valueOf(firstRateMid).toString())
            Assert.assertFalse(thirdItem.isRateDiffBiggerThanAverage)

        }
    }
}