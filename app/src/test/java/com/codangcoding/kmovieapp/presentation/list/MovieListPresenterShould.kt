package com.codangcoding.kmovieapp.presentation.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.codangcoding.kmovieapp.domain.data.MovieRepository
import com.codangcoding.kmovieapp.domain.entity.Movie
import com.codangcoding.kmovieapp.presentation.list.MovieListContract.ViewState.*
import com.codangcoding.kmovieapp.util.TestAppDispatchers
import com.codangcoding.kmovieapp.util.TestObserver
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.Mockito.`when` as whenever

@RunWith(MockitoJUnitRunner.StrictStubs::class)
class MovieListPresenterShould {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val repository = Mockito.mock(MovieRepository::class.java)

    private val job = Job()
    private val presenter = MovieListPresenter(repository, job, TestAppDispatchers)

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
            .thenReturn(movies)

        val observer = TestObserver<MovieListContract.ViewState>()
        presenter.viewStates.observeForever(observer)

        presenter.loadPopularMovies()

        observer.assertThatHistoryAt(0, LoadingState)
        observer.assertThatHistoryAt(1, ResultState(movies))
    }

    @Test
    fun send_error_state_when_load_popular_movies_failed() = runBlocking {
        whenever(repository.getPopularMovies())
            .thenThrow(IllegalArgumentException("Anone anone"))

        val observer = TestObserver<MovieListContract.ViewState>()
        presenter.viewStates.observeForever(observer)

        presenter.loadPopularMovies()

        observer.assertThatHistoryAt(0, LoadingState)
        observer.assertThatHistoryAt(1, ErrorState("Anone anone"))
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
            .thenReturn(movies)

        val observer = TestObserver<MovieListContract.ViewState>()
        presenter.viewStates.observeForever(observer)

        presenter.loadNowPlayingMovies()

        observer.assertThatHistoryAt(0, LoadingState)
        observer.assertThatHistoryAt(1, ResultState(movies))
    }

    @Test
    fun send_error_state_when_load_now_playing_movies_failed() = runBlocking {
        whenever(repository.getNowPlayingMovies())
            .thenThrow(IllegalArgumentException("Anone anone"))

        val observer = TestObserver<MovieListContract.ViewState>()
        presenter.viewStates.observeForever(observer)

        presenter.loadNowPlayingMovies()

        observer.assertThatHistoryAt(0, LoadingState)
        observer.assertThatHistoryAt(1, ErrorState("Anone anone"))
    }

    @Test
    fun on_cleared_should_cancel_job() {
        assertEquals(job.isCancelled, false)

        presenter.onCleared()

        assertEquals(job.isCancelled, true)
    }
}