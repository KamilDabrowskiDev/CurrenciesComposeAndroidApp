package com.currencies.compose.app.ui.screen.currencies.mvi

import androidx.lifecycle.viewModelScope
import com.currencies.compose.app.common.mvi.BaseViewModel
import com.currencies.compose.app.logic.Result
import com.currencies.compose.app.logic.currencies.CurrenciesHandler
import com.currencies.compose.app.ui.common.other.Debouncer
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


/**
 * Copyright(c) 2024 Kamil Dąbrowski. All rights reserved
 */


@HiltViewModel
class CurrenciesViewModel @Inject constructor(
    private val currenciesHandler: CurrenciesHandler
) : BaseViewModel<CurrenciesViewEvent, CurrenciesViewAction>() {

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
        loadCurrencies()
    }

    private fun loadCurrencies() {
        viewModelScope.launch {

            onAction(CurrenciesViewAction.OnAverageCurrenciesLoadStarted)

            delay(400)

            val result = currenciesHandler.getCurrencyItems()

            when (result) {
                is Result.Success -> {
                    onAction(CurrenciesViewAction.OnAverageCurrenciesLoadSuccess(result.data))
                }

                is Result.Failure -> {
                    onAction(CurrenciesViewAction.OnAverageCurrenciesLoadError)
                }
            }
        }
    }

    override fun onEvent(event: CurrenciesViewEvent) {
        super.onEvent(event)

        when (event) {

            CurrenciesViewEvent.BackPressed -> {
                onAction(CurrenciesViewAction.OnExitNeeded)
            }

            is CurrenciesViewEvent.CurrencyItemClicked -> {
                currencyItemClickDebounce.invoke(event) {
                    onAction(CurrenciesViewAction.OnCurrencyDetailNeedToBeOpened(event.item))
                }
            }

            CurrenciesViewEvent.RetryButtonClicked -> {
                retryButtonDebounce.invoke(event) {
                    loadCurrencies()
                }
            }
        }
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

    override fun onAction(action: CurrenciesViewAction) {
        super.onAction(action)

        when (action) {

            CurrenciesViewAction.OnAverageCurrenciesLoadError -> {
                _state.update {
                    it.copy(
                        loadCurrenciesState = AverageCurrenciesLoadState.LoadError
                    )
                }
            }

            CurrenciesViewAction.OnAverageCurrenciesLoadStarted -> {
                _state.update {
                    it.copy(
                        loadCurrenciesState = AverageCurrenciesLoadState.LoadStarted
                    )
                }
            }

            is CurrenciesViewAction.OnAverageCurrenciesLoadSuccess -> {
                _state.update {
                    it.copy(
                        loadCurrenciesState = AverageCurrenciesLoadState.LoadSuccess(action.items)
                    )
                }
            }

            is CurrenciesViewAction.OnCurrencyDetailNeedToBeOpened -> {
                val item = action.item

                sendEffect(
                    CurrenciesViewEffect.OpenCurrencyDetail(
                        currencyTableName = item.currencyTableName,
                        currencyName = item.currencyName,
                        currencyCode = item.currencyCode,
                        currencyAverageExchangeRatePLN = item.averageExchangeRatePLN
                    )
                )
            }

            CurrenciesViewAction.OnExitNeeded -> {
                sendEffect(CurrenciesViewEffect.ExitApp)
            }
        }
    }
}