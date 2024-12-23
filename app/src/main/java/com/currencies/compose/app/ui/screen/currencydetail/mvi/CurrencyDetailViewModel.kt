package com.currencies.compose.app.ui.screen.currencydetail.mvi

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.currencies.compose.app.common.mvi.BaseViewModel
import com.currencies.compose.app.logic.Result
import com.currencies.compose.app.logic.historicalexchangerate.DateRangeCalculator
import com.currencies.compose.app.logic.historicalexchangerate.HistoricalExchangeRateHandler
import com.currencies.compose.app.ui.common.other.Debouncer
import com.currencies.compose.app.ui.screen.CurrencyDetailRoute
import com.currencies.compose.app.ui.screen.currencydetail.model.CurrencyHistoryListState
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
) : BaseViewModel<CurrencyDetailViewEvent, CurrencyDetailViewAction>() {

    private val retryDebounce = Debouncer<CurrencyDetailViewEvent>(viewModelScope)

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
        onAction(CurrencyDetailViewAction.OnInitDataRefreshed(
            currencyName = passedRoute.currencyName,
            currencyCode = passedRoute.currencyCode,
            currencyAverageExchangeValue = BigDecimal.valueOf(passedRoute.currencyAverageExchangeRatePLN)
        ))

        loadCurrencyHistoryItems()
    }


    private fun loadCurrencyHistoryItems() {
        viewModelScope.launch {

            onAction(CurrencyDetailViewAction.OnCurrencyHistoryItemsLoadStarted)

            delay(400)

            val queryDateRange = dateRangeCalculator.getDateRangeFromTimestamp(
                timestamp = Clock.System.now().toEpochMilliseconds(),
                daysBefore = DAYS_BEFORE_CURRENT_DATE
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
                    onAction(CurrencyDetailViewAction.OnCurrencyHistoryItemsLoadSuccess(historicalRates))
                }

                is Result.Failure -> {
                    onAction(CurrencyDetailViewAction.OnCurrencyHistoryItemsLoadError)
                }
            }
        }
    }

    override fun onEvent(event: CurrencyDetailViewEvent) {
        super.onEvent(event)

        when (event) {
            CurrencyDetailViewEvent.BackPressed -> {
                onAction(CurrencyDetailViewAction.OnExitNeeded)
            }

            CurrencyDetailViewEvent.RetryButtonClicked -> {
                retryDebounce.invoke(event) { event ->
                    loadCurrencyHistoryItems()
                }
            }
        }
    }

    override fun onAction(action: CurrencyDetailViewAction) {
        super.onAction(action)

        when (action) {

            is CurrencyDetailViewAction.OnInitDataRefreshed -> {
                _state.update {
                    it.copy(
                        currencyName = action.currencyName,
                        currencyCode = action.currencyCode,
                        currencyAverageExchangeRatePLN = action.currencyAverageExchangeValue
                    )
                }
            }

            CurrencyDetailViewAction.OnCurrencyHistoryItemsLoadStarted -> {
                _state.update {
                    it.copy(
                        currencyHistoryListState = CurrencyHistoryListState.LoadStarted
                    )
                }
            }

            is CurrencyDetailViewAction.OnCurrencyHistoryItemsLoadSuccess -> {
                _state.update {
                    it.copy(
                        currencyHistoryListState = CurrencyHistoryListState.LoadSuccess(
                            items = action.items
                        )
                    )
                }
            }

            CurrencyDetailViewAction.OnCurrencyHistoryItemsLoadError -> {
                _state.update {
                    it.copy(
                        currencyHistoryListState = CurrencyHistoryListState.LoadError
                    )
                }
            }

            CurrencyDetailViewAction.OnExitNeeded -> {
                sendEffect(CurrencyDetailViewEffect.Exit)
            }
        }
    }

    private fun sendEffect(effect: CurrencyDetailViewEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }

    companion object {
        const val DAYS_BEFORE_CURRENT_DATE = 14
    }
}