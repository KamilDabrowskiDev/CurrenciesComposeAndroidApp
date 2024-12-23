package com.currencies.compose.app.ui.screen.currencydetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.currencies.compose.app.common.utils.EventLogger
import com.currencies.compose.app.ui.common.other.ObserveEffects
import com.currencies.compose.app.ui.screen.currencydetail.mvi.CurrencyDetailViewEffect
import com.currencies.compose.app.ui.screen.currencydetail.mvi.CurrencyDetailViewModel

/**
 * Copyright(c) 2024 Kamil Dąbrowski. All rights reserved
 */

@Composable
fun CurrencyDetailScreen(navController: NavController) {

    val viewModel = hiltViewModel<CurrencyDetailViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    EventLogger.logState(state)

    ObserveEffects(viewModel.effect) { effect ->
        EventLogger.logEffect(effect)

        when (effect) {
            CurrencyDetailViewEffect.Exit -> {
                navController.popBackStack()
            }
        }
    }

    CurrencyDetailScreenContent(state = state, onEvent = viewModel::onEvent)
}