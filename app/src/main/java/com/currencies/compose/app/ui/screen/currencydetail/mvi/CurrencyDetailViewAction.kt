package com.currencies.compose.app.ui.screen.currencydetail.mvi

import com.currencies.compose.app.ui.screen.currencydetail.model.HistoricalCurrencyExchangeRateItem
import java.math.BigDecimal

/**
 * Copyright(c) 2024 Kamil Dąbrowski. All rights reserved
 */

sealed class CurrencyDetailViewAction {

    data class OnInitDataRefreshed(
        val currencyName: String,
        val currencyCode: String,
        val currencyAverageExchangeValue: BigDecimal
    ) : CurrencyDetailViewAction()

    object OnCurrencyHistoryItemsLoadStarted : CurrencyDetailViewAction()

    data class OnCurrencyHistoryItemsLoadSuccess(
        val items: List<HistoricalCurrencyExchangeRateItem>
    ) : CurrencyDetailViewAction()

    object OnCurrencyHistoryItemsLoadError : CurrencyDetailViewAction()

    object OnExitNeeded : CurrencyDetailViewAction()

}