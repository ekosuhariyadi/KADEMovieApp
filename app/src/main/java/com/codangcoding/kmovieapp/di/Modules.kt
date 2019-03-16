package com.codangcoding.kmovieapp.di

import com.codangcoding.kmovieapp.BuildConfig
import com.codangcoding.kmovieapp.domain.data.MovieRepository
import com.codangcoding.kmovieapp.domain.data.MovieRepositoryImpl
import com.codangcoding.kmovieapp.external.data.MovieService
import com.codangcoding.kmovieapp.presentation.list.MovieListContract
import com.codangcoding.kmovieapp.presentation.list.MovieListPresenter
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

val appModule = module {
    single<Retrofit> {
        val objectMapper = ObjectMapper()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .registerKotlinModule()

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val originRequest = chain.request()
                val originUrl = originRequest.url()

                val newUrl = originUrl.newBuilder()
                    .addQueryParameter("api_key", BuildConfig.API_KEY)
                    .addQueryParameter("language", "en-US")
                    .build()
                val newRequest = originRequest.newBuilder()
                    .url(newUrl)
                    .build()

                chain.proceed(newRequest)
            }
            .build()

        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

    single<MovieService> {
        get<Retrofit>().create(MovieService::class.java)
    }

    single<MovieRepository> {
        MovieRepositoryImpl(get())
    }
}

val movieListModule = module {
    factory<MovieListContract.Presenter> {
        MovieListPresenter(get())
    }
}