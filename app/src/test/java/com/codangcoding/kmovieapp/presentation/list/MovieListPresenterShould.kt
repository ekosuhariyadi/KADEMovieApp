package com.codangcoding.kmovieapp.presentation.list

import com.codangcoding.kmovieapp.domain.data.MovieRepository
import com.codangcoding.kmovieapp.domain.entity.Movie
import com.codangcoding.kmovieapp.presentation.list.MovieListContract.ViewState.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.Mockito.`when` as whenever

@RunWith(MockitoJUnitRunner.StrictStubs::class)
class MovieListPresenterShould {

    private val repository = Mockito.mock(MovieRepository::class.java)

    private val presenter = MovieListPresenter(repository)

    @Test
    fun send_result_when_load_popular_movies_success() = runBlocking {
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
        whenever(repository.getPopularMovies())
            .thenReturn(CompletableDeferred(movies))

        val viewStateChannel = presenter.viewStates()
        launch {
            presenter.loadPopularMovies()
        }

        val actualStates = mutableListOf<MovieListContract.ViewState>()
        repeat(2) { actualStates.add(viewStateChannel.receive()) }

        assertEquals(LoadingState, actualStates[0])
        assertTrue(actualStates[1] is ResultState)
        assertEquals(movies, (actualStates[1] as ResultState).movies)
    }

    @Test
    fun send_error_state_when_load_popular_movies_failed() = runBlocking {
        whenever(repository.getPopularMovies())
            .thenThrow(IllegalArgumentException("Anone anone"))

        val viewStateChannel = presenter.viewStates()
        launch {
            presenter.loadPopularMovies()
        }

        val actualStates = mutableListOf<MovieListContract.ViewState>()
        repeat(2) { actualStates.add(viewStateChannel.receive()) }

        assertEquals(LoadingState, actualStates[0])
        assertTrue(actualStates[1] is ErrorState)
        assertEquals("Anone anone", (actualStates[1] as ErrorState).error)
    }

    @Test
    fun send_result_when_load_now_playing_movies_success() = runBlocking {
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
        whenever(repository.getNowPlayingMovies())
            .thenReturn(CompletableDeferred(movies))

        val viewStateChannel = presenter.viewStates()
        launch {
            presenter.loadNowPlayingMovies()
        }

        val actualStates = mutableListOf<MovieListContract.ViewState>()
        repeat(2) { actualStates.add(viewStateChannel.receive()) }

        assertEquals(LoadingState, actualStates[0])
        assertTrue(actualStates[1] is ResultState)
        assertEquals(movies, (actualStates[1] as ResultState).movies)
    }

    @Test
    fun send_error_state_when_load_now_playing_movies_failed() = runBlocking {
        whenever(repository.getNowPlayingMovies())
            .thenThrow(IllegalArgumentException("Anone anone"))

        val viewStateChannel = presenter.viewStates()
        launch {
            presenter.loadNowPlayingMovies()
        }

        val actualStates = mutableListOf<MovieListContract.ViewState>()
        repeat(2) { actualStates.add(viewStateChannel.receive()) }

        assertEquals(LoadingState, actualStates[0])
        assertTrue(actualStates[1] is ErrorState)
        assertEquals("Anone anone", (actualStates[1] as ErrorState).error)
    }
}