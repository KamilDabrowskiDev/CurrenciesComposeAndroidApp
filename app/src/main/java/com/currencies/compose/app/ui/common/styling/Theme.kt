package com.currencies.compose.app.ui.common.styling

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.currencies.compose.app.ui.common.composeresources.Pink40
import com.currencies.compose.app.ui.common.composeresources.Purple40
import com.currencies.compose.app.ui.common.composeresources.PurpleGrey40


private val AppColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {

    MaterialTheme(
        colorScheme = AppColorScheme,
        typography = Typography,
        content = content
    )
}