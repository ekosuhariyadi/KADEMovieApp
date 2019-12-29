package com.codangcoding.kmovieapp.external.data

import com.codangcoding.kmovieapp.domain.entity.ApiResponse
import retrofit2.http.GET

interface MovieService {

    @GET("popular")
    suspend fun getPopularMovies(): ApiResponse

    @GET("now_playing")
    suspend fun getNowPlayingMovies(): ApiResponse
}