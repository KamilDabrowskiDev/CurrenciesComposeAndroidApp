
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Copyright(c) 2024 Kamil Dąbrowski. All rights reserved
 */

class DispatcherProviderImpl : DispatcherProvider {

    override fun io(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    override fun default(): CoroutineDispatcher {
        return Dispatchers.Default
    }

    override fun main(): CoroutineDispatcher {
        return Dispatchers.Main.immediate
    }
}