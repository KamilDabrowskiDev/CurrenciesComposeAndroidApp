package com.currencies.compose.app.ui.common.other

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Copyright(c) 2024 Kamil Dąbrowski. All rights reserved
 */

class Debouncer<T>(
    private val scope: CoroutineScope,
    private val debounceTimeMillis: Long = 600L
) {
    private var debounceJob: Job? = null

    fun invoke(event: T, onEvent: (T) -> Unit) {
        debounceJob?.cancel()

        debounceJob = scope.launch {
            delay(debounceTimeMillis)
            onEvent(event)
        }
    }
}
