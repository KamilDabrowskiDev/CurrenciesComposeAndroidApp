package com.currencies.compose.app.ui.screen.currencydetail.mvi

import com.currencies.compose.app.ui.screen.currencydetail.model.CurrencyHistoryListState
import java.math.BigDecimal

/**
 * Copyright(c) 2024 Kamil Dąbrowski. All rights reserved
 */

data class CurrencyDetailViewState(
    val currencyName: String,
    val currencyCode: String,
    val currencyAverageExchangeRatePLN: BigDecimal,
    val currencyHistoryListState: CurrencyHistoryListState,
) {

    val currencyAverageExchangeRatePLNText = currencyAverageExchangeRatePLN.toString()

    companion object {
        fun initial(): CurrencyDetailViewState {
            return CurrencyDetailViewState(
                currencyName = "",
                currencyCode = "",
                currencyAverageExchangeRatePLN = BigDecimal(0.0),
                currencyHistoryListState = CurrencyHistoryListState.LoadStarted,
            )
        }
    }
}