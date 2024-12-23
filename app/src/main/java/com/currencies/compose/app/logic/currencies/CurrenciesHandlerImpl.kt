package com.currencies.compose.app.logic.currencies

import com.currencies.compose.app.common.async.DispatcherProvider
import com.currencies.compose.app.common.utils.AppLogger
import com.currencies.compose.app.data.NBPApiRepository
import com.currencies.compose.app.data.request.GetCurrentExchangeTableReqParams
import com.currencies.compose.app.data.validator.CurrentExchangeRateTableApiEntityValidator
import com.currencies.compose.app.ui.screen.currencies.CurrencyItem
import com.currencies.compose.app.logic.ErrorType
import com.currencies.compose.app.logic.Result
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext

/**
 * Copyright(c) 2024 Kamil Dąbrowski. All rights reserved
 */

class CurrenciesHandlerImpl(
    private val apiRepository: NBPApiRepository,
    private val dispatcherProvider: DispatcherProvider,
    private val currencyItemMapper: CurrencyItemMapper,
    private val tableEntityValidator: CurrentExchangeRateTableApiEntityValidator
) : CurrenciesHandler {


    override suspend fun getCurrencyItems(): Result<List<CurrencyItem>> {
        return withContext(dispatcherProvider.io()) {
            supervisorScope {

                try {

                    val tableAEntitiesDeferred = async {
                        apiRepository.getCurrentExchangeRateTable(
                            GetCurrentExchangeTableReqParams(tableName = "A")
                        )
                    }

                    val tableBEntitiesDeferred = async {
                        apiRepository.getCurrentExchangeRateTable(
                            GetCurrentExchangeTableReqParams(tableName = "B")
                        )
                    }

                    val tableAEntities = tableAEntitiesDeferred.await()
                    val tableBEntities = tableBEntitiesDeferred.await()

                    val tableAEntity = tableAEntities[0]
                    val tableBEntity = tableBEntities[0]

                    tableEntityValidator.validate(tableAEntity)
                    tableEntityValidator.validate(tableBEntity)

                    val currencyARates = tableAEntity.rates
                    val currencyBRates = tableBEntity.rates

                    val tableAItems = currencyARates.map { currencyItemMapper.from(it, "A") }
                    val tableBItems = currencyBRates.map { currencyItemMapper.from(it, "B") }

                    val mergedItems = tableAItems + tableBItems

                    return@supervisorScope Result.Success(mergedItems)

                } catch (exception: IndexOutOfBoundsException) {
                    AppLogger.errorLog("ERROR - A or B table was empty")
                    coroutineContext.cancelChildren()
                    return@supervisorScope Result.Failure(errorType = ErrorType.EmptyResponseError)
                } catch (exception: Exception) {
                    AppLogger.errorLog("ERROR when preparing CurrencyItems: $exception")
                    coroutineContext.cancelChildren()
                    return@supervisorScope Result.Failure(errorType = ErrorType.NotSpecifiedError)
                }
            }
        }
    }
}