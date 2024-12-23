package com.currencies.compose.app.data.error

/**
 * Copyright(c) 2024 Kamil Dąbrowski. All rights reserved
 */

class InvalidApiFieldException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause) 