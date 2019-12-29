package com.codangcoding.kmovieapp.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

object TestAppDispatchers : AppDispatchers {

    override val io: CoroutineDispatcher
        get() = Dispatchers.Unconfined

    override val ui: CoroutineDispatcher
        get() = Dispatchers.Unconfined

    override val computation: CoroutineDispatcher
        get() = Dispatchers.Unconfined
}