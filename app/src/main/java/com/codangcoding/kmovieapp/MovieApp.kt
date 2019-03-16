package com.codangcoding.kmovieapp

import android.app.Application
import com.codangcoding.kmovieapp.di.appModule
import com.codangcoding.kmovieapp.di.movieListModule
import org.koin.android.ext.android.startKoin

class MovieApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin(
            this,
            listOf(
                appModule,
                movieListModule
            )
        )
    }
}