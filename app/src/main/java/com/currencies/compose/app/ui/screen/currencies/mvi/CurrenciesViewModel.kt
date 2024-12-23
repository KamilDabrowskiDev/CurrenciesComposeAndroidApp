package com.currencies.compose.app.ui.screen.currencies.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.currencies.compose.app.common.async.DispatcherProvider
import com.currencies.compose.app.common.utils.AppLogger
import com.currencies.compose.app.common.utils.EventLogger
import com.currencies.compose.app.logic.currencies.CurrenciesHandler
import com.currencies.compose.app.ui.common.other.Debouncer
import com.currencies.compose.app.ui.screen.currencies.CurrencyItem
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
import javax.inject.Inject
import com.currencies.compose.app.logic.Result


/**
 * Copyright(c) 2024 Kamil Dąbrowski. All rights reserved
 */


@HiltViewModel
class CurrenciesViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val currenciesHandler: CurrenciesHandler
) : ViewModel() {

    private val currencyItemClickDebounce = Debouncer<CurrenciesViewEvent>(viewModelScope)
    private val retryButtonDebounce = Debouncer<CurrenciesViewEvent>(viewModelScope)

    private val _effect = Channel<CurrenciesViewEffect>()
    val effect = _effect.receiveAsFlow()

    private val _state = MutableStateFlow(CurrenciesViewState.initial())
    val state = _state
        .onStart {
            initialize()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = CurrenciesViewState.initial()
        )

    private fun initialize() {
        viewModelScope.launch {
            loadCurrencies()
        }
    }

    private fun loadCurrencies() {
        viewModelScope.launch {


            onAverageCurrenciesLoadStarted()

            delay(400)

            val result = currenciesHandler.getCurrencyItems()

            when (result) {
                is Result.Success -> {
                    onAverageCurrenciesLoadSuccess(result.data)
                }

                is Result.Failure -> {
                    onAverageCurrenciesLoadError()
                }
            }
        }
    }


    fun onEvent(event: CurrenciesViewEvent) {
        EventLogger.logEvent(event)

        when (event) {

            is CurrenciesViewEvent.CurrencyItemClicked -> {
                currencyItemClickDebounce.invoke(event) {
                    handleCurrencyItemClicked(event.item)
                }
            }

            CurrenciesViewEvent.RetryButtonClicked -> {
                retryButtonDebounce.invoke(event) {
                    loadCurrencies()
                }
            }

            CurrenciesViewEvent.BackPressed -> {
                onExitNeeded()
            }
        }
    }

    private fun handleCurrencyItemClicked(currencyItem: CurrencyItem) {
        onCurrencyDetailNeedToBeOpened(currencyItem)
    }

    ///////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////
    // UI interaction methods
    ///////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////

    private fun sendEffect(effect: CurrenciesViewEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }

    private fun onAverageCurrenciesLoadStarted() {
        _state.update {
            it.copy(
                loadCurrenciesState = AverageCurrenciesLoadState.LoadStarted
            )
        }
    }

    private fun onAverageCurrenciesLoadSuccess(items: List<CurrencyItem>) {
        _state.update {
            it.copy(
                loadCurrenciesState = AverageCurrenciesLoadState.LoadSuccess(items)
            )
        }
    }

    private fun onAverageCurrenciesLoadError() {
        _state.update {
            it.copy(
                loadCurrenciesState = AverageCurrenciesLoadState.LoadError
            )
        }
    }

    private fun onCurrencyDetailNeedToBeOpened(
        item: CurrencyItem
    ) {
        sendEffect(
            CurrenciesViewEffect.OpenCurrencyDetail(
                currencyTableName = item.currencyTableName,
                currencyName = item.currencyName,
                currencyCode = item.currencyCode,
                currencyAverageExchangeRatePLN = item.averageExchangeRatePLN
            )
        )
    }

    private fun onExitNeeded() {
        sendEffect(CurrenciesViewEffect.ExitApp)
    }
}