package com.codangcoding.kmovieapp.domain.data

import com.codangcoding.kmovieapp.domain.entity.ApiResponse
import com.codangcoding.kmovieapp.domain.entity.Movie
import com.codangcoding.kmovieapp.external.data.MovieService
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.Mockito.`when` as whenever

@RunWith(MockitoJUnitRunner::class)
class MovieRepositoryImplShould {

    private val service = mock(MovieService::class.java)

    private val repository = MovieRepositoryImpl(service)

    @Test
    fun return_movie_when_get_popular_movies_success() = runBlocking {
        val movies = listOf(
            Movie(
                title = "Kamen Rider Ichigo da Movie",
                vote = 9.9,
                overview = "The first Kamen Rider on big screen",
                releaseDate = "1986-10-10",
                posterPath = "/url_poster",
                backdropPath = "/url_backdrop"
            ),
            Movie(
                title = "Hikaru no Go: Winter Soldier",
                vote = 9.7,
                overview = "Fates make Hikaru meet Sai again in a winter",
                releaseDate = "1986-10-10",
                posterPath = "/url_hikaru_vs_akira_toya",
                backdropPath = "/url_sai_vs_toya_meijin"
            )
        )

        val response =
            ApiResponse(page = 1, totalPage = 1, totalResult = 2, movies = movies)

        whenever(service.getPopularMovies())
            .thenReturn(CompletableDeferred(response))

        val actualMovies = repository.getPopularMovies().await()

        assertEquals(movies, actualMovies)
    }

    @Test(expected = IllegalArgumentException::class)
    fun throw_exception_when_get_popular_movies_failed() = runBlocking {
        whenever(service.getPopularMovies())
            .thenThrow(IllegalArgumentException("Something went wrong heh"))

        repository.getPopularMovies().await()

        Unit
    }

    @Test
    fun return_movie_when_get_now_playing_movies_success() = runBlocking {
        val movies = listOf(
            Movie(
                title = "Kamen Rider Ichigo da Movie",
                vote = 9.9,
                overview = "The first Kamen Rider on big screen",
                releaseDate = "1986-10-10",
                posterPath = "/url_poster",
                backdropPath = "/url_backdrop"
            ),
            Movie(
                title = "Hikaru no Go: Winter Soldier",
                vote = 9.7,
                overview = "Fates make Hikaru meet Sai again in a winter",
                releaseDate = "1986-10-10",
                posterPath = "/url_hikaru_vs_akira_toya",
                backdropPath = "/url_sai_vs_toya_meijin"
            )
        )

        val response =
            ApiResponse(page = 1, totalPage = 1, totalResult = 2, movies = movies)

        whenever(service.getNowPlayingMovies())
            .thenReturn(CompletableDeferred(response))

        val actualMovies = repository.getNowPlayingMovies().await()

        assertEquals(movies, actualMovies)
    }

    @Test(expected = IllegalArgumentException::class)
    fun throw_exception_when_get_now_playing_movies_failed() = runBlocking {
        whenever(service.getNowPlayingMovies())
            .thenThrow(IllegalArgumentException("Something went wrong heh"))

        repository.getNowPlayingMovies().await()

        Unit
    }

}