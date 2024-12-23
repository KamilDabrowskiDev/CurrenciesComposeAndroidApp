package com.currencies.compose.app.ui.screen.currencies.mvi

import com.currencies.compose.app.ui.screen.currencies.model.CurrencyItem


/**
 * Copyright(c) 2024 Kamil Dąbrowski. All rights reserved
 */

sealed class CurrenciesViewEvent {

    object BackPressed : CurrenciesViewEvent()

    data class CurrencyItemClicked(val item: CurrencyItem) : CurrenciesViewEvent()

    object RetryButtonClicked : CurrenciesViewEvent()

}