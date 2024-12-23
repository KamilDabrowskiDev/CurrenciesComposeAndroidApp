package com.currencies.compose.app.ui.screen.currencydetail.mvi

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.currencies.compose.app.common.utils.EventLogger
import com.currencies.compose.app.logic.Result
import com.currencies.compose.app.logic.historicalexchangerate.DateRangeCalculator
import com.currencies.compose.app.logic.historicalexchangerate.HistoricalExchangeRateHandler
import com.currencies.compose.app.ui.screen.CurrencyDetailRoute
import com.currencies.compose.app.ui.screen.currencydetail.model.CurrencyHistoryListState
import com.currencies.compose.app.ui.screen.currencydetail.model.HistoricalCurrencyExchangeRateItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import java.math.BigDecimal
import javax.inject.Inject


/**
 * Copyright(c) 2024 Kamil Dąbrowski. All rights reserved
 */


@HiltViewModel
class CurrencyDetailViewModel @Inject constructor(
    private val historicalExchangeRateHandler: HistoricalExchangeRateHandler,
    private val dateRangeCalculator: DateRangeCalculator,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val passedRoute = savedStateHandle.toRoute<CurrencyDetailRoute>()

    private val _effect = Channel<CurrencyDetailViewEffect>()
    val effect = _effect.receiveAsFlow()

    private val _state = MutableStateFlow(CurrencyDetailViewState.initial())
    val state = _state.onStart {
        initialize()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = CurrencyDetailViewState.initial()
    )

    private fun initialize() {

        onRefreshInitData(
            currencyName = passedRoute.currencyName,
            currencyCode = passedRoute.currencyCode,
            currencyAverageExchangeValue = BigDecimal.valueOf(passedRoute.currencyAverageExchangeRatePLN)
        )

        loadCurrencyHistoryItems()
    }


    private fun loadCurrencyHistoryItems() {
        viewModelScope.launch {

            onCurrencyHistoryItemsLoadStarted()

            delay(400)

            val queryDateRange = dateRangeCalculator.getDateRangeFromTimestamp(
                timestamp = Clock.System.now().toEpochMilliseconds(),
                daysBefore = 14
            )

            val result = historicalExchangeRateHandler.getHistoricalExchangeRates(
                currencyCode = passedRoute.currencyCode,
                tableName = passedRoute.currencyTableName,
                queryDateRange = queryDateRange,
                currentAverageExchangeRate = BigDecimal.valueOf(passedRoute.currencyAverageExchangeRatePLN)
            )

            when (result) {
                is Result.Success -> {
                    val historicalRates = result.data
                    onCurrencyHistoryItemsLoadSuccess(historicalRates)
                }

                is Result.Failure -> {
                    onCurrencyHistoryItemsLoadError()
                }
            }
        }
    }

    fun onEvent(event: CurrencyDetailViewEvent) {
        EventLogger.logEvent(event)

        when (event) {
            CurrencyDetailViewEvent.BackPressed -> {
                onExitNeeded()
            }

            CurrencyDetailViewEvent.RetryButtonClicked -> {
                loadCurrencyHistoryItems()
            }
        }
    }

    ////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////
    // UI interaction methods
    ////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////

    private fun sendEffect(effect: CurrencyDetailViewEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }

    private fun onRefreshInitData(
        currencyName: String,
        currencyCode: String,
        currencyAverageExchangeValue: BigDecimal
    ) {
        _state.update {
            it.copy(
                currencyName = currencyName,
                currencyCode = currencyCode,
                currencyAverageExchangeRatePLN = currencyAverageExchangeValue
            )
        }
    }

    private fun onCurrencyHistoryItemsLoadStarted() {
        _state.update {
            it.copy(
                currencyHistoryListState = CurrencyHistoryListState.LoadStarted
            )
        }
    }

    private fun onCurrencyHistoryItemsLoadSuccess(items: List<HistoricalCurrencyExchangeRateItem>) {
        _state.update {
            it.copy(
                currencyHistoryListState = CurrencyHistoryListState.LoadSuccess(
                    items = items
                )
            )
        }
    }

    private fun onCurrencyHistoryItemsLoadError() {
        _state.update {
            it.copy(
                currencyHistoryListState = CurrencyHistoryListState.LoadError
            )
        }
    }

    private fun onExitNeeded() {
        sendEffect(CurrencyDetailViewEffect.Exit)
    }

}