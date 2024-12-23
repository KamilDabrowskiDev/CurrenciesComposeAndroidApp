package com.currencies.compose.app.common.utils


/**
 * Copyright(c) 2024 Kamil Dąbrowski. All rights reserved
 */
class EventLogger {

    companion object {

        fun logEvent(event: Any) {
            val trackLog = "EVENT: $event"
            AppLogger.mviLog(trackLog)
        }

        fun logState(state: Any) {
            val trackLog = "STATE: $state"
            AppLogger.mviLog(trackLog)
        }

        fun logEffect(effect: Any) {
            val trackLog = "EFFECT: $effect"
            AppLogger.mviLog(trackLog)
        }
    }
}