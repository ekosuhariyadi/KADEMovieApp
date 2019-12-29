package com.codangcoding.kmovieapp.presentation.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.codangcoding.kmovieapp.domain.entity.Movie

interface MovieListContract {

    abstract class Presenter : ViewModel() {

        abstract val viewStates: LiveData<ViewState>

        abstract fun loadPopularMovies()

        abstract fun loadNowPlayingMovies()
    }

    interface View {

        fun renderState(viewState: ViewState)
    }

    sealed class ViewState {
        object LoadingState : ViewState()
        data class ResultState(val movies: List<Movie>) : ViewState()
        data class ErrorState(val error: String) : ViewState()
    }
}