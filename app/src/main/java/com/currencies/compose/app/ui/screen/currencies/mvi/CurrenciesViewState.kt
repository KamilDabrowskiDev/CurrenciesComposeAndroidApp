package com.currencies.compose.app.ui.screen.currencies.mvi

import com.currencies.compose.app.ui.screen.currencies.model.CurrencyItem


/**
 * Copyright(c) 2024 Kamil Dąbrowski. All rights reserved
 */
data class CurrenciesViewState(
    val loadCurrenciesState: AverageCurrenciesLoadState
) {

    companion object {
        fun initial(): CurrenciesViewState {
            return CurrenciesViewState(AverageCurrenciesLoadState.LoadStarted)
        }
    }
}

sealed class AverageCurrenciesLoadState {

    object LoadStarted : AverageCurrenciesLoadState()

    data class LoadSuccess(val items: List<CurrencyItem>) : AverageCurrenciesLoadState()

    object LoadError : AverageCurrenciesLoadState()

}