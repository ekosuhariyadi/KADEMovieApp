package com.codangcoding.kmovieapp.domain.data

import com.codangcoding.kmovieapp.domain.entity.Movie
import com.codangcoding.kmovieapp.external.data.MovieService
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.async

interface MovieRepository {

    fun getPopularMovies(): Deferred<List<Movie>>

    fun getNowPlayingMovies(): Deferred<List<Movie>>
}

class MovieRepositoryImpl(
    private val service: MovieService
) : MovieRepository {

    override fun getPopularMovies(): Deferred<List<Movie>> =
        GlobalScope.async {
            service.getPopularMovies().await().movies
        }

    override fun getNowPlayingMovies(): Deferred<List<Movie>> =
        GlobalScope.async {
            service.getNowPlayingMovies().await().movies
        }

}