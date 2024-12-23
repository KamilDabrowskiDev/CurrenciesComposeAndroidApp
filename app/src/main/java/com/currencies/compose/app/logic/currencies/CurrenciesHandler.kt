package com.currencies.compose.app.logic.currencies

import com.currencies.compose.app.logic.Result
import com.currencies.compose.app.ui.screen.currencies.model.CurrencyItem


/**
 * Copyright(c) 2024 Kamil Dąbrowski. All rights reserved
 */
interface CurrenciesHandler {

    suspend fun getCurrencyItems(): Result<List<CurrencyItem>>

}