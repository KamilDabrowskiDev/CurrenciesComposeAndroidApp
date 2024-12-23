package com.currencies.compose.app.ui.common.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.currencies.compose.app.R
import com.currencies.compose.app.ui.common.composeresources.AppColors
import com.currencies.compose.app.ui.common.styling.AppTheme

/**
 * Copyright(c) 2024 Kamil Dąbrowski. All rights reserved
 */

enum class AppToolbarEvent {
    BACK_PRESSED
}

@Composable
fun AppToolbar(
    onEvent: (AppToolbarEvent) -> Unit,
    title: String,
    modifier: Modifier
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(AppColors.ForestGreen)

    ) {

        Box(modifier = Modifier
            .fillMaxHeight()
            .width(60.dp)
            .clickable {
                onEvent.invoke(AppToolbarEvent.BACK_PRESSED)
            }
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_arrow_back),
                modifier = Modifier
                    .size(36.dp)
                    .align(Alignment.Center),
                contentDescription = "",
                colorFilter = ColorFilter.tint(Color.White)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            contentAlignment = Alignment.CenterStart
        ) {

            Column() {
                Text(
                    text = title,
                    fontSize = 20.sp,
                    color = Color.White
                )
            }
        }
    }
}

@Preview
@Composable
fun AppToolbarPreview() {
    AppTheme {
        AppToolbar(
            title = "Example text",
            onEvent = {},
            modifier = Modifier
        )
    }
}
