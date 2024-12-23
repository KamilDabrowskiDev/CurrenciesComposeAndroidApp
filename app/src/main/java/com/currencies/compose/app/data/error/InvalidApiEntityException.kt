package com.currencies.compose.app.data.error

/**
 * Copyright(c) 2024 Kamil Dąbrowski. All rights reserved
 */

class InvalidApiEntityException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause)