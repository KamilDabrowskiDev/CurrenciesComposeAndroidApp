package com.currencies.compose.app.ui.screen.currencies

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.currencies.compose.app.common.utils.EventLogger
import com.currencies.compose.app.ui.common.other.ObserveEffects
import com.currencies.compose.app.ui.screen.CurrencyDetailRoute
import com.currencies.compose.app.ui.screen.currencies.mvi.CurrenciesViewEffect
import com.currencies.compose.app.ui.screen.currencies.mvi.CurrenciesViewModel
import finishIfPossible

/**
 * Copyright(c) 2024 Kamil Dąbrowski. All rights reserved
 */

@Composable
fun CurrenciesScreen(navController: NavController) {

    val viewModel = hiltViewModel<CurrenciesViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    EventLogger.logState(state)

    ObserveEffects(viewModel.effect) { effect ->

        EventLogger.logEffect(effect)

        when (effect) {

            CurrenciesViewEffect.ExitApp -> {
                context.finishIfPossible()
            }

            is CurrenciesViewEffect.OpenCurrencyDetail -> {
                navController.navigate(
                    CurrencyDetailRoute(
                        currencyTableName = effect.currencyTableName ,
                        currencyName = effect.currencyName,
                        currencyCode = effect.currencyCode,
                        currencyAverageExchangeRatePLN = effect.currencyAverageExchangeRatePLN
                    )
                )
            }
        }
    }

    CurrenciesScreenContent(state, viewModel::onEvent)
}