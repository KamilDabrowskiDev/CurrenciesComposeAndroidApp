package com.currencies.compose.app.data.logging

import com.currencies.compose.app.common.utils.AppLogger
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor

/**
 * Copyright(c) 2024 Kamil Dąbrowski. All rights reserved
 */

class SimpleLoggingInterceptor : Interceptor {

    private val logger = HttpLoggingInterceptor.Logger { message ->
        AppLogger.httpLog(message)
    }

    private val loggingInterceptor = HttpLoggingInterceptor(logger).apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        AppLogger.httpLog("REQUEST: ${request.method} ${request.url}")
        request.headers.forEach { header ->
            AppLogger.httpLog("Request header: $header")
        }

        val response = chain.proceed(request)

        AppLogger.httpLog("RESPONSE: ${response.code} ${response.message}")
        response.headers.forEach { header ->
            AppLogger.httpLog("Response header: $header")
        }

        return response
    }
}