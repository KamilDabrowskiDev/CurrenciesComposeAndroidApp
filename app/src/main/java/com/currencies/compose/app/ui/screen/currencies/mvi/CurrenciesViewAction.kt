package com.currencies.compose.app.ui.screen.currencies.mvi

import com.currencies.compose.app.ui.screen.currencies.CurrencyItem

/**
 * Copyright(c) 2024 Kamil Dąbrowski. All rights reserved
 */

sealed class CurrenciesViewAction {

    object OnAverageCurrenciesLoadStarted : CurrenciesViewAction()

    data class OnAverageCurrenciesLoadSuccess(
        val items: List<CurrencyItem>
    ) : CurrenciesViewAction()

    object OnAverageCurrenciesLoadError : CurrenciesViewAction()

    data class OnCurrencyDetailNeedToBeOpened(val item: CurrencyItem) : CurrenciesViewAction()

    object OnExitNeeded : CurrenciesViewAction()

}