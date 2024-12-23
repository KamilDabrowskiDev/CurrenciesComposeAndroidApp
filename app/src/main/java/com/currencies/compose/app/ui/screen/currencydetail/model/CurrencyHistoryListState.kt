package com.currencies.compose.app.ui.screen.currencydetail.model


/**
 * Copyright(c) 2024 Kamil Dąbrowski. All rights reserved
 */

sealed class CurrencyHistoryListState {

    object LoadStarted : CurrencyHistoryListState()

    data class LoadSuccess(val items: List<HistoricalCurrencyExchangeRateItem>) : CurrencyHistoryListState()

    object LoadError : CurrencyHistoryListState()

}