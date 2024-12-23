package com.currencies.compose.app.logic.currencies

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.currencies.compose.app.common.async.DispatcherProvider
import com.currencies.compose.app.data.NBPApiRepository
import com.currencies.compose.app.data.request.GetCurrentExchangeTableReqParams
import com.currencies.compose.app.data.response.CurrentExchangeRateApiEntity
import com.currencies.compose.app.data.response.CurrentExchangeRateTableApiEntity
import com.currencies.compose.app.data.validator.CurrentExchangeRateTableApiEntityValidator
import com.currencies.compose.app.logic.ErrorType
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import javax.inject.Inject

import com.currencies.compose.app.logic.Result
import com.currencies.compose.app.ui.screen.currencies.model.CurrencyItem


/**
 * Copyright(c) 2024 Kamil Dąbrowski. All rights reserved
 */

@HiltAndroidTest
@Config(application = HiltTestApplication::class)
@RunWith(AndroidJUnit4::class)
class CurrenciesHandlerTest {


    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    private val mockedNBPApiRepository = mockk<NBPApiRepository>()

    @Inject
    lateinit var dispatcherProvider: DispatcherProvider

    @Inject
    lateinit var currencyItemMapper: CurrencyItemMapper

    @Inject
    lateinit var tableApiEntityValidator: CurrentExchangeRateTableApiEntityValidator

    lateinit var currenciesHandler: CurrenciesHandler

    @Before
    fun before() {
        hiltRule.inject()

        currenciesHandler = CurrenciesHandlerImpl(
            apiRepository = mockedNBPApiRepository,
            dispatcherProvider = dispatcherProvider,
            currencyItemMapper = currencyItemMapper,
            tableEntityValidator = tableApiEntityValidator
        )
    }

    @Test
    fun `getCurrencyItems, loadA FAILED - loadB OK, return ResultFailure`() {
        runBlocking {

            //  when
            coEvery {
                mockedNBPApiRepository.getCurrentExchangeRateTable(
                    GetCurrentExchangeTableReqParams("A")
                )
            } throws RuntimeException()

            coEvery {
                mockedNBPApiRepository.getCurrentExchangeRateTable(
                    GetCurrentExchangeTableReqParams("B")
                )
            } returns generateMockedExchangeRateTables("B")


            //  given
            val result = currenciesHandler.getCurrencyItems()

            //  then
            Assert.assertTrue(result is Result.Failure)
        }
    }

    @Test
    fun `getCurrencyItems, loadA OK - loadB FAILED, return ResultFailure`() {
        runBlocking {

            coEvery {
                mockedNBPApiRepository.getCurrentExchangeRateTable(
                    GetCurrentExchangeTableReqParams("A")
                )
            } returns generateMockedExchangeRateTables("A")

            coEvery {
                mockedNBPApiRepository.getCurrentExchangeRateTable(
                    GetCurrentExchangeTableReqParams("B")
                )
            } throws RuntimeException()

            //  given
            val result = currenciesHandler.getCurrencyItems()

            //  then
            Assert.assertTrue(result is Result.Failure)
        }
    }

    @Test
    fun `getCurrencyItems, loadA FAILED - loadB FAILED, return ResultFailure`() {
        runBlocking {

            //  when
            coEvery {
                mockedNBPApiRepository.getCurrentExchangeRateTable(
                    GetCurrentExchangeTableReqParams("A")
                )
            } throws RuntimeException()

            coEvery {
                mockedNBPApiRepository.getCurrentExchangeRateTable(
                    GetCurrentExchangeTableReqParams("B")
                )
            } throws RuntimeException()

            //  given
            val result = currenciesHandler.getCurrencyItems()

            //  then
            Assert.assertTrue(result is Result.Failure)
        }
    }

    @Test
    fun `getCurrencyItems, loadA EMPTY - loadB EMPTY, return ResultFailure`() {
        runBlocking {

            //  when
            coEvery {
                mockedNBPApiRepository.getCurrentExchangeRateTable(
                    GetCurrentExchangeTableReqParams("A")
                )
            } returns emptyList()

            coEvery {
                mockedNBPApiRepository.getCurrentExchangeRateTable(
                    GetCurrentExchangeTableReqParams("B")
                )
            } returns emptyList()


            //  given
            val result = currenciesHandler.getCurrencyItems()

            //  then
            Assert.assertTrue(result is Result.Failure)
            val errorType = (result as Result.Failure).errorType
            Assert.assertTrue(errorType == ErrorType.EmptyResponseError)
        }
    }


    @Test
    fun `getCurrencyItems, loadA OK - loadB EMPTY , return ResultFailure`() {
        runBlocking {

            //  when
            coEvery {
                mockedNBPApiRepository.getCurrentExchangeRateTable(
                    GetCurrentExchangeTableReqParams("A")
                )
            } returns generateMockedExchangeRateTables("A")

            coEvery {
                mockedNBPApiRepository.getCurrentExchangeRateTable(
                    GetCurrentExchangeTableReqParams("B")
                )
            } returns emptyList()

            //  given
            val result = currenciesHandler.getCurrencyItems()

            //  then
            Assert.assertTrue(result is Result.Failure)
            val errorType = (result as Result.Failure).errorType
            Assert.assertTrue(errorType == ErrorType.EmptyResponseError)
        }
    }

    @Test
    fun `getCurrencyItems, loadA EMPTY - loadB OK, return ResultFailure`() {
        runBlocking {

            //  when
            coEvery {
                mockedNBPApiRepository.getCurrentExchangeRateTable(
                    GetCurrentExchangeTableReqParams("A")
                )
            } returns emptyList()

            coEvery {
                mockedNBPApiRepository.getCurrentExchangeRateTable(
                    GetCurrentExchangeTableReqParams("B")
                )
            } returns generateMockedExchangeRateTables("B")

            //  given
            val result = currenciesHandler.getCurrencyItems()

            //  then
            Assert.assertTrue(result is Result.Failure)
            val errorType = (result as Result.Failure).errorType
            Assert.assertTrue(errorType == ErrorType.EmptyResponseError)
        }
    }


    @Test
    fun `getCurrencyItems, loadA OK - loadB OK, return ResultSuccess`() {
        runBlocking {

            //  when
            val mockedTableA = CurrentExchangeRateTableApiEntity(
                table = "A",
                number = "242/A/NBP/2024",
                effectiveDate = "2024-12-13",
                rates = listOf(
                    CurrentExchangeRateApiEntity("FIRST", "USD", 4.0738),
                    CurrentExchangeRateApiEntity("SECOND", "EUR", 4.2722),
                    CurrentExchangeRateApiEntity("THIRD", "CHF", 4.5534),
                )
            )

            val mockedTableB = CurrentExchangeRateTableApiEntity(
                table = "B",
                number = "050/B/NBP/2024",
                effectiveDate = "2024-12-11",
                rates = listOf(
                    CurrentExchangeRateApiEntity("FOURTH", "ZWG", 0.1559),
                    CurrentExchangeRateApiEntity("FIFTH", "BDT", 0.00245),
                    CurrentExchangeRateApiEntity("SIXTH", "MNT", 10.005),
                )
            )

            coEvery {
                mockedNBPApiRepository.getCurrentExchangeRateTable(
                    GetCurrentExchangeTableReqParams("A")
                )
            } returns listOf(mockedTableA)

            coEvery {
                mockedNBPApiRepository.getCurrentExchangeRateTable(
                    GetCurrentExchangeTableReqParams("B")
                )
            } returns listOf(mockedTableB)


            //  given
            val result = currenciesHandler.getCurrencyItems()

            //  then
            Assert.assertTrue(result is Result.Success)
        }
    }


    @Test
    fun `getCurrencyItems, loadA OK - loadB OK, return MERGED TABLES with proper data`() {
        runBlocking {

            val tableA = "A"
            val tableB = "B"

            val firstCurrencyName = "FIRST"
            val firstCurrencyCode = "USD"
            val firstCurrencyMid = 4.0738

            val secondCurrencyName = "SECOND"
            val secondCurrencyCode = "EUR"
            val secondCurrencyMid = 4.2722

            val thirdCurrencyName = "THIRD"
            val thirdCurrencyCode = "CHF"
            val thirdCurrencyMid = 4.5534

            val fourthCurrencyName = "FOURTH"
            val fourthCurrencyCode = "ZWG"
            val fourthCurrencyMid = 0.1559

            val fifthCurrencyName = "FIFTH"
            val fifthCurrencyCode = "BDT"
            val fifthCurrencyMid = 0.00245

            val sixthCurrencyName = "SIXTH"
            val sixthCurrencyCode = "MNT"
            val sixthCurrencyMid = 10.005

            val mockedTableA = CurrentExchangeRateTableApiEntity(
                table = tableA,
                number = "242/A/NBP/2024",
                effectiveDate = "2024-12-13",
                rates = listOf(
                    CurrentExchangeRateApiEntity(firstCurrencyName, firstCurrencyCode, firstCurrencyMid),
                    CurrentExchangeRateApiEntity(secondCurrencyName, secondCurrencyCode, secondCurrencyMid),
                    CurrentExchangeRateApiEntity(thirdCurrencyName, thirdCurrencyCode, thirdCurrencyMid),
                )
            )

            val mockedTableB = CurrentExchangeRateTableApiEntity(
                table = tableB,
                number = "050/B/NBP/2024",
                effectiveDate = "2024-12-11",
                rates = listOf(
                    CurrentExchangeRateApiEntity(fourthCurrencyName, fourthCurrencyCode, fourthCurrencyMid),
                    CurrentExchangeRateApiEntity(fifthCurrencyName, fifthCurrencyCode, fifthCurrencyMid),
                    CurrentExchangeRateApiEntity(sixthCurrencyName, sixthCurrencyCode, sixthCurrencyMid),
                )
            )

            coEvery {
                mockedNBPApiRepository.getCurrentExchangeRateTable(
                    GetCurrentExchangeTableReqParams("A")
                )
            } returns listOf(mockedTableA)

            coEvery {
                mockedNBPApiRepository.getCurrentExchangeRateTable(
                    GetCurrentExchangeTableReqParams("B")
                )
            } returns listOf(mockedTableB)


            //  given
            val result = currenciesHandler.getCurrencyItems()


            //  then
            Assert.assertTrue(result is Result.Success)
            val currencyItems = (result as Result.Success<List<CurrencyItem>>).data
            Assert.assertTrue(currencyItems.size == 6)

            val firstItem = currencyItems[0]
            val secondItem = currencyItems[1]
            val thirdItem = currencyItems[2]
            val fourthItem = currencyItems[3]
            val fifthItem = currencyItems[4]
            val sixthItem = currencyItems[5]

            Assert.assertTrue(firstItem.currencyTableName == tableA)
            Assert.assertTrue(firstItem.currencyName == firstCurrencyName)
            Assert.assertTrue(firstItem.currencyCode == firstCurrencyCode)
            Assert.assertTrue(firstItem.averageExchangeRatePLN == firstCurrencyMid)

            Assert.assertTrue(secondItem.currencyTableName == tableA)
            Assert.assertTrue(secondItem.currencyName == secondCurrencyName)
            Assert.assertTrue(secondItem.currencyCode == secondCurrencyCode)
            Assert.assertTrue(secondItem.averageExchangeRatePLN == secondCurrencyMid)

            Assert.assertTrue(thirdItem.currencyTableName == tableA)
            Assert.assertTrue(thirdItem.currencyName == thirdCurrencyName)
            Assert.assertTrue(thirdItem.currencyCode == thirdCurrencyCode)
            Assert.assertTrue(thirdItem.averageExchangeRatePLN == thirdCurrencyMid)

            Assert.assertTrue(fourthItem.currencyTableName == tableB)
            Assert.assertTrue(fourthItem.currencyName == fourthCurrencyName)
            Assert.assertTrue(fourthItem.currencyCode == fourthCurrencyCode)
            Assert.assertTrue(fourthItem.averageExchangeRatePLN == fourthCurrencyMid)

            Assert.assertTrue(fifthItem.currencyTableName == tableB)
            Assert.assertTrue(fifthItem.currencyName == fifthCurrencyName)
            Assert.assertTrue(fifthItem.currencyCode == fifthCurrencyCode)
            Assert.assertTrue(fifthItem.averageExchangeRatePLN == fifthCurrencyMid)

            Assert.assertTrue(sixthItem.currencyTableName == tableB)
            Assert.assertTrue(sixthItem.currencyName == sixthCurrencyName)
            Assert.assertTrue(sixthItem.currencyCode == sixthCurrencyCode)
            Assert.assertTrue(sixthItem.averageExchangeRatePLN == sixthCurrencyMid)
        }
    }

    /////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////


    private fun generateMockedExchangeRateTables(tableName: String): List<CurrentExchangeRateTableApiEntity> {

        val instant = Clock.System.now()
        val effectiveDate = instant.toLocalDateTime(TimeZone.UTC).date

        val formattedEffectiveDate = effectiveDate.toString()

        val tableNumber = "242/A/NBP/2024"

        val rates = listOf(
            CurrentExchangeRateApiEntity("Dollar", "USD", 1.0),
            CurrentExchangeRateApiEntity("Euro", "EUR", 0.94),
            CurrentExchangeRateApiEntity("Pound", "GBP", 0.82),
            CurrentExchangeRateApiEntity("Swiss Franc", "CHF", 0.93),
            CurrentExchangeRateApiEntity("Yen", "JPY", 142.5)
        )

        return listOf(
            CurrentExchangeRateTableApiEntity(
                table = tableName,
                number = tableNumber,
                effectiveDate = formattedEffectiveDate,
                rates = rates
            )
        )
    }


}