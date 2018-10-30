package com.codangcoding.kmovieapp.external.data

import com.codangcoding.kmovieapp.domain.entity.ApiResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface MovieService {

    @GET("popular")
    fun getPopularMovies(): Deferred<ApiResponse>

    @GET("now_playing")
    fun getNowPlayingMovies(): Deferred<ApiResponse>
}