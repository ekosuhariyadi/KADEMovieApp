package com.codangcoding.kmovieapp.presentation.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.codangcoding.kmovieapp.domain.data.MovieRepository
import com.codangcoding.kmovieapp.presentation.list.MovieListContract.ViewState.*
import com.codangcoding.kmovieapp.util.AppDispatchers
import kotlinx.coroutines.*

class MovieListPresenter(
    private val repository: MovieRepository,
    private val job: Job,
    private val appDispatchers: AppDispatchers
) : MovieListContract.Presenter() {

    private val _viewStates = MutableLiveData<MovieListContract.ViewState>()
    override val viewStates: LiveData<MovieListContract.ViewState>
        get() = _viewStates

    private val coroutineScope = CoroutineScope(appDispatchers.ui + SupervisorJob(job))

    override fun loadPopularMovies() {
        coroutineScope.launch {
            try {
                _viewStates.value = LoadingState
                val movies = withContext(appDispatchers.io) {
                    repository.getPopularMovies()
                }
                _viewStates.value = ResultState(movies)
            } catch (ex: Exception) {
                if (ex !is CancellationException)
                    _viewStates.value = ErrorState(ex.message ?: "")
            }
        }
    }

    override fun loadNowPlayingMovies() {
        coroutineScope.launch {
            try {
                _viewStates.value = LoadingState
                val movies = withContext(appDispatchers.io) {
                    repository.getNowPlayingMovies()
                }
                _viewStates.value = ResultState(movies)
            } catch (ex: Exception) {
                if (ex !is CancellationException)
                    _viewStates.value = ErrorState(ex.message ?: "")
            }
        }
    }

    public override fun onCleared() {
        job.cancel()
    }
}