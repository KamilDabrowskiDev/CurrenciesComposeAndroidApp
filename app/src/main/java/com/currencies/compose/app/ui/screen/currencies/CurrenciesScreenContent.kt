package com.currencies.compose.app.ui.screen.currencies

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.currencies.compose.app.R
import com.currencies.compose.app.logic.mocks.MockedItemsFactory
import com.currencies.compose.app.ui.common.components.OutlinedFrame
import com.currencies.compose.app.ui.common.composeresources.AppColors
import com.currencies.compose.app.ui.common.styling.AppTheme
import com.currencies.compose.app.ui.screen.currencies.model.CurrencyItem
import com.currencies.compose.app.ui.screen.currencies.mvi.AverageCurrenciesLoadState
import com.currencies.compose.app.ui.screen.currencies.mvi.CurrenciesViewEvent
import com.currencies.compose.app.ui.screen.currencies.mvi.CurrenciesViewState

/**
 * Copyright(c) 2024 Kamil Dąbrowski. All rights reserved
 */


@Composable
fun CurrenciesScreenContent(
    state: CurrenciesViewState,
    onEvent: (event: CurrenciesViewEvent) -> Unit
) {

    BackHandler {
        onEvent.invoke(CurrenciesViewEvent.BackPressed)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.LightForestGreen),
        contentAlignment = Alignment.Center
    ) {

        Column(Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(AppColors.ForestGreen)
                    .padding(start = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = stringResource(R.string.exchange_rates),
                    fontSize = 20.sp,
                    color = Color.White
                )
            }

            ContentContainer(
                state = state.loadCurrenciesState,
                onEvent = onEvent,
                modifier = Modifier,
            )
        }
    }
}

@Composable
fun ContentContainer(
    state: AverageCurrenciesLoadState,
    onEvent: (event: CurrenciesViewEvent) -> Unit,
    modifier: Modifier
) {
    when (state) {

        AverageCurrenciesLoadState.LoadStarted -> {
            ChildCurrenciesLoadStarted(
                modifier = modifier
            )
        }

        is AverageCurrenciesLoadState.LoadSuccess -> {
            ChildCurrenciesLoadSuccess(
                onEvent = onEvent,
                items = state.items,
                modifier = modifier
            )
        }

        is AverageCurrenciesLoadState.LoadError -> {
            ChildCurrenciesLoadError(
                modifier = modifier,
                onEvent = onEvent
            )
        }
    }
}

@Composable
fun ChildCurrenciesLoadStarted(modifier: Modifier) {
    Box(modifier = modifier.fillMaxSize()) {

        CircularProgressIndicator(
            color = AppColors.ForestGreen,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(bottom = 36.dp)
                .size(52.dp),
            strokeWidth = 4.dp
        )
    }
}

@Composable
fun ChildCurrenciesLoadError(
    modifier: Modifier,
    onEvent: (event: CurrenciesViewEvent) -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Column {
            Text(
                text = stringResource(R.string.error_message_load_exchange_rates),
                textAlign = TextAlign.Center,
                fontSize = 16.sp
            )

            Button(modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(24.dp),
                onClick = {
                    onEvent.invoke(CurrenciesViewEvent.RetryButtonClicked)
                }) {

                Text(text = stringResource(R.string.action_try_again).uppercase())
            }
        }
    }
}

@Composable
fun ChildCurrenciesLoadSuccess(
    onEvent: (event: CurrenciesViewEvent) -> Unit,
    items: List<CurrencyItem>,
    modifier: Modifier,
    leftMargin: Dp = 12.dp,
    rightMargin: Dp = 12.dp,
    topMargin: Dp = 0.dp,
    bottomMargin: Dp = 16.dp,
    firstTopMargin: Dp = 16.dp,
    lastBottomMargin: Dp = 16.dp
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {

        itemsIndexed(items) { index, item ->

            val isFirstItem = index == 0
            val isLastItem = index == items.size

            CurrencyListItem(
                item = item,
                onEvent = onEvent,
                modifier = Modifier.padding(
                    start = leftMargin,
                    end = rightMargin,
                    top = if (isFirstItem) firstTopMargin else topMargin,
                    bottom = if (isLastItem) lastBottomMargin else bottomMargin
                ),
            )
        }
    }
}

@Composable
fun CurrencyListItem(
    onEvent: (event: CurrenciesViewEvent) -> Unit,
    item: CurrencyItem,
    modifier: Modifier,
) {

    OutlinedFrame(
        color = AppColors.ForestGreen,
        modifier = modifier.clickable {
            onEvent.invoke(CurrenciesViewEvent.CurrencyItemClicked(item))
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.currencyName,
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
            )
            Text(
                text = item.averageExchangeRatePLNText,
                modifier = Modifier.padding(8.dp),
            )
        }
    }
}

@Composable
@Preview
fun CurrencyListItemPreview() {
    AppTheme {
        CurrencyListItem(
            modifier = Modifier.padding(
                start = 8.dp,
                end = 8.dp,
                top = 8.dp,
                bottom = 8.dp
            ),
            item = CurrencyItem(
                currencyTableName = "A",
                currencyName = "Dolar amerykański",
                currencyCode = "USD",
                averageExchangeRatePLN = 4.1543
            ),
            onEvent = {

            },
        )

    }
}


@Composable
@Preview(locale = "pl")
@Preview(locale = "pl", widthDp = 407, heightDp = 724)
@Preview(locale = "pl", widthDp = 600, heightDp = 960)
fun CurrenciesScreenContentPreview() {
    AppTheme {
        CurrenciesScreenContent(
            state = CurrenciesViewState.initial().copy(

//                loadCurrenciesState = AverageCurrenciesLoadState.LoadStarted
//                loadCurrenciesState = AverageCurrenciesLoadState.LoadError
                loadCurrenciesState = AverageCurrenciesLoadState.LoadSuccess(
                    items = MockedItemsFactory.mockedCurrencyItems()
                )
            ),
            onEvent = {}
        )
    }
}