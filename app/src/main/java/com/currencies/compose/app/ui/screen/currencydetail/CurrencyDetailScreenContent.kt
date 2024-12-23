package com.currencies.compose.app.ui.screen.currencydetail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.currencies.compose.app.R
import com.currencies.compose.app.ui.common.components.AppToolbar
import com.currencies.compose.app.ui.common.composeresources.AppColors
import com.currencies.compose.app.ui.common.styling.AppTheme
import com.currencies.compose.app.ui.screen.currencydetail.model.CurrencyHistoryListState
import com.currencies.compose.app.ui.screen.currencydetail.model.HistoricalCurrencyExchangeRateItem
import com.currencies.compose.app.ui.screen.currencydetail.mvi.CurrencyDetailViewEvent
import com.currencies.compose.app.ui.screen.currencydetail.mvi.CurrencyDetailViewState
import java.math.BigDecimal

/**
 * Copyright(c) 2024 Kamil Dąbrowski. All rights reserved
 */

@Composable
fun CurrencyDetailScreenContent(
    onEvent: (event: CurrencyDetailViewEvent) -> Unit,
    state: CurrencyDetailViewState
) {

    BackHandler {
        onEvent.invoke(CurrencyDetailViewEvent.BackPressed)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.LightForestGreen),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {

            AppToolbar(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding(),
                title = stringResource(R.string.currency_history),
                onEvent = {
                    onEvent.invoke(CurrencyDetailViewEvent.BackPressed)
                },
            )

            CurrencyInfoContainer(
                currencyName = state.currencyName,
                currencyCode = state.currencyCode,
                currentExchangeRatePLNText = state.currencyAverageExchangeRatePLNText
            )

            CurrencyHistoryContainer(
                onEvent = onEvent,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentState = state.currencyHistoryListState
            )
        }
    }
}

@Composable
fun CurrencyInfoContainer(
    currencyName: String,
    currencyCode: String,
    currentExchangeRatePLNText: String
) {
    Column(
        modifier = Modifier
            .wrapContentHeight()
    ) {
        Text(
            text = currencyName,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(
                start = 16.dp,
                top = 16.dp,
                end = 16.dp
            )
        )

        Text(
            modifier = Modifier.padding(
                start = 16.dp,
                top = 6.dp,
                bottom = 8.dp
            ),
            fontWeight = FontWeight.Bold,
            text = currencyCode,
        )

        Text(
            modifier = Modifier.padding(
                start = 16.dp,
                top = 4.dp,
                bottom = 12.dp
            ),
            fontWeight = FontWeight.Bold,
            text = currentExchangeRatePLNText,
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(AppColors.ForestGreen)
        )
    }
}


@Composable
fun CurrencyHistoryContainer(
    modifier: Modifier,
    contentState: CurrencyHistoryListState,
    onEvent: (event: CurrencyDetailViewEvent) -> Unit
) {
    Box(modifier = modifier) {
        when (contentState) {

            CurrencyHistoryListState.LoadStarted -> {
                ContentChildLoadStarted(modifier)
            }


            is CurrencyHistoryListState.LoadSuccess -> {
                ContentChildLoadSuccess(
                    onEvent = onEvent,
                    items = contentState.items,
                    modifier = modifier
                )
            }

            CurrencyHistoryListState.LoadError -> {
                ContentChildLoadError(
                    onEvent = onEvent,
                    modifier = modifier
                )
            }
        }
    }
}

@Composable
fun ContentChildLoadStarted(modifier: Modifier) {
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
fun ContentChildLoadError(
    onEvent: (event: CurrencyDetailViewEvent) -> Unit,
    modifier: Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Column {
            Text(
                text = stringResource(R.string.error_message_load_historical_exchange_rates),
                textAlign = TextAlign.Center,
                fontSize = 16.sp
            )

            Button(modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(24.dp),
                onClick = {
                    onEvent.invoke(CurrencyDetailViewEvent.RetryButtonClicked)
                }) {

                Text(text = stringResource(R.string.action_try_again).uppercase())
            }
        }
    }

}

@Composable
fun ContentChildLoadSuccess(
    onEvent: (event: CurrencyDetailViewEvent) -> Unit,
    items: List<HistoricalCurrencyExchangeRateItem>,
    modifier: Modifier,
    leftMargin: Dp = 0.dp,
    rightMargin: Dp = 0.dp,
    topMargin: Dp = 0.dp,
    bottomMargin: Dp = 0.dp,
    firstTopMargin: Dp = 0.dp,
    lastBottomMargin: Dp = 16.dp
) {
    LazyColumn(modifier = modifier) {
        itemsIndexed(items) { index, item ->

            val isFirstItem = index == 0
            val isLastItem = index == items.size

            CurrencyHistoryItem(
                onEvent = onEvent,
                item = item,
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
fun CurrencyHistoryItem(
    onEvent: (event: CurrencyDetailViewEvent) -> Unit,
    item: HistoricalCurrencyExchangeRateItem,
    modifier: Modifier,
) {

    Column(
        modifier = modifier
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 24.dp, bottom = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.dateText,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 24.dp)
            )
            Text(
                text = item.exchangeRateText,
                color = item.exchangeRateFontColor,
                fontWeight = item.exchangeRateFontWeight,
                modifier = Modifier.padding(end = 24.dp)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(AppColors.ForestGreen)
        )


    }
}

@Composable
@Preview
fun CurrencyHistoryItemPreview() {
    AppTheme {
        CurrencyHistoryItem(
            onEvent = {},
            modifier = Modifier,
            item = HistoricalCurrencyExchangeRateItem(
                dateText = "10 grudnia 2024",
                dateTimestamp = System.currentTimeMillis(),
                exchangeRateText = "4.14 PLN",
                isRateDiffBiggerThanAverage = false
            )
        )
    }
}

@Composable
@Preview(locale = "pl")
@Preview(widthDp = 407, heightDp = 724)
@Preview(widthDp = 600, heightDp = 960)
fun CurrencyDetailScreenContentPreview() {
    AppTheme {
        CurrencyDetailScreenContent(
            onEvent = {},
            state = CurrencyDetailViewState.initial().copy(
                currencyName = "Dolar amerykański",
                currencyCode = "USD",
                currencyAverageExchangeRatePLN = BigDecimal("4.10"),
                currencyHistoryListState = CurrencyHistoryListState.LoadSuccess(
                    items = buildList {
                        add(
                            HistoricalCurrencyExchangeRateItem(
                                dateText = "1 luty 2024",
                                dateTimestamp = System.currentTimeMillis(),
                                exchangeRateText = "2.4 PLN",
                                isRateDiffBiggerThanAverage = true
                            )
                        )

                    }
                )
            )
        )
    }
}

