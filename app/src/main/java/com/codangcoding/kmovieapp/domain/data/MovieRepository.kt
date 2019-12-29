package com.codangcoding.kmovieapp.domain.data

import com.codangcoding.kmovieapp.domain.entity.Movie
import com.codangcoding.kmovieapp.external.data.MovieService

interface MovieRepository {

    suspend fun getPopularMovies(): List<Movie>

    suspend fun getNowPlayingMovies(): List<Movie>
}

class MovieRepositoryImpl(
    private val service: MovieService
) : MovieRepository {

    override suspend fun getPopularMovies(): List<Movie> =
        service.getPopularMovies().movies

    override suspend fun getNowPlayingMovies(): List<Movie> =
        service.getNowPlayingMovies().movies

}