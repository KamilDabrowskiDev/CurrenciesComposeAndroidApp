package com.currencies.compose.app.common.mvi

import androidx.lifecycle.ViewModel
import com.currencies.compose.app.common.utils.AppLogger

/**
 * Copyright(c) 2024 Kamil Dąbrowski. All rights reserved
 */

abstract class BaseViewModel<Event, Action>() : ViewModel() {

    open fun onEvent(event: Event) {
        val eventLog = "EVENT: $event"
        AppLogger.mviLog(eventLog)
    }

    protected open fun onAction(action: Action) {
        val actionLog = "ACTION: $action"
        AppLogger.mviLog(actionLog)
    }
}