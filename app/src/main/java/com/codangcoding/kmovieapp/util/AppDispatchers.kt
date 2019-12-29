package com.codangcoding.kmovieapp.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface AppDispatchers {

    val io: CoroutineDispatcher

    val ui: CoroutineDispatcher

    val computation: CoroutineDispatcher
}

object DefaultAppDispatcher : AppDispatchers {

    override val io: CoroutineDispatcher
        get() = Dispatchers.IO

    override val ui: CoroutineDispatcher
        get() = Dispatchers.Main

    override val computation: CoroutineDispatcher
        get() = Dispatchers.Default
}