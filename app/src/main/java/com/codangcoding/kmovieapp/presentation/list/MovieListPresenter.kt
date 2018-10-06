package com.codangcoding.kmovieapp.presentation.list

import com.codangcoding.kmovieapp.domain.data.MovieRepository
import com.codangcoding.kmovieapp.presentation.list.MovieListContract.ViewState.*
import kotlinx.coroutines.experimental.CancellationException
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.launch

class MovieListPresenter(
    private val repository: MovieRepository
) : MovieListContract.Presenter() {

    private val compositeJob = Job()
        get() {
            return if (field.isCancelled)
                Job() // canceled job can be reused, so create a new
            else field
        }

    private val viewStates = Channel<MovieListContract.ViewState>()

    override fun viewStates(): Channel<MovieListContract.ViewState> =
        viewStates

    override fun loadPopularMovies() {
        GlobalScope.launch(compositeJob) {
            try {
                viewStates.send(LoadingState)
                val movies = repository.getPopularMovies().await()
                viewStates.send(ResultState(movies))
            } catch (ex: Exception) {
                if (ex !is CancellationException)
                    viewStates.send(ErrorState(ex.message ?: ""))
            }
        }
    }

    override fun loadNowPlayingMovies() {
        GlobalScope.launch(compositeJob) {
            try {
                viewStates.send(LoadingState)
                val movies = repository.getNowPlayingMovies().await()
                viewStates.send(ResultState(movies))
            } catch (ex: Exception) {
                if (ex !is CancellationException)
                    viewStates.send(ErrorState(ex.message ?: ""))
            }
        }
    }

    override fun onCleared() {
        compositeJob.cancel()
        viewStates.cancel()
    }
}