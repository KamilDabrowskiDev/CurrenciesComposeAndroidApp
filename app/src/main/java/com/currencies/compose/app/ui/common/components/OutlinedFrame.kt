package com.currencies.compose.app.ui.common.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Copyright(c) 2024 Kamil Dąbrowski. All rights reserved
 */

@Composable
fun OutlinedFrame(
    modifier: Modifier,
    color: Color,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .border(
                width = 2.dp,
                color = color,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(8.dp)
    ) {
        content()
    }
}