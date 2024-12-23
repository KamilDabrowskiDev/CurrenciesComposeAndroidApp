
import kotlinx.coroutines.CoroutineDispatcher

/**
 * Copyright(c) 2024 Kamil Dąbrowski. All rights reserved
 */

interface DispatcherProvider {

    fun io(): CoroutineDispatcher

    fun default(): CoroutineDispatcher

    fun main(): CoroutineDispatcher
}