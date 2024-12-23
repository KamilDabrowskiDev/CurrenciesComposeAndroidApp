package com.currencies.compose.app.common.utils

import android.util.Log
import com.currencies.compose.app.BuildConfig

/**
 * Copyright(c) 2024 Kamil Dąbrowski. All rights reserved
 */

class AppLogger {

    companion object {

        const val APP_VERBOSE_TAG = "APP_VERBOSE_TAG"
        const val APP_DEBUG_TAG = "APP_DEBUG_TAG"
        const val APP_ERROR_TAG = "APP_ERROR_TAG"
        const val APP_MVI_TAG = "APP_MVI_TAG"
        const val APP_HTTP_TAG = "APP_HTTP_TAG"

        fun verboseLog(message: String, tag: String = APP_VERBOSE_TAG) {
            if (BuildConfig.VERBOSE_LOGS_ENABLED) {
                Log.d(tag, message)
            }
        }

        fun debugLog(message: String, tag: String = APP_DEBUG_TAG) {
            if (BuildConfig.DEBUG_LOGS_ENABLED) {
                Log.d(tag, message)
            }
        }

        fun errorLog(message: String, tag: String = APP_ERROR_TAG) {
            if (BuildConfig.ERROR_LOGS_ENABLED) {
                Log.d(tag, message)
            }
        }

        fun mviLog(message: String, tag: String = APP_MVI_TAG) {
            verboseLog(message)

            if (BuildConfig.MVI_LOGS_ENABLED) {
                Log.d(tag, message)
            }
        }

        fun httpLog(message: String, tag: String = APP_HTTP_TAG) {
            verboseLog(message)

            if (BuildConfig.HTTP_LOGS_ENABLED) {
                Log.d(tag, message)
            }
        }
    }

}