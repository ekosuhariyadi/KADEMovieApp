package com.codangcoding.kmovieapp.util

import androidx.lifecycle.Observer
import org.junit.Assert.assertEquals

class TestObserver<T> : Observer<T> {

    private val history = mutableListOf<T>()

    override fun onChanged(t: T?) {
        t?.let { history.add(it) }
    }

    fun assertThatHistoryAt(position: Int, t: T) {
        assertEquals(history[position], t)
    }
}