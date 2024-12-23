package com.currencies.compose.app.logic

/**
 * Copyright(c) 2024 Kamil Dąbrowski. All rights reserved
 */

sealed class Result<out T> {

    data class Success<out T>(val data: T) : Result<T>()

    data class Failure<out T>(val errorType: ErrorType, val result: T? = null) : Result<T>()
}

sealed class ErrorType {

    object NotSpecifiedError : ErrorType()
    object EmptyResponseError: ErrorType()

}
