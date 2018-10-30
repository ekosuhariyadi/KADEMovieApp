package com.codangcoding.kmovieapp.presentation.list

import android.arch.lifecycle.ViewModel
import com.codangcoding.kmovieapp.domain.entity.Movie
import kotlinx.coroutines.channels.ReceiveChannel

interface MovieListContract {

    abstract class Presenter : ViewModel() {

        abstract fun viewStates(): ReceiveChannel<ViewState>

        abstract fun loadPopularMovies()

        abstract fun loadNowPlayingMovies()
    }

    interface View {

        fun renderState(viewState: ViewState)
    }

    sealed class ViewState {
        object LoadingState : ViewState()
        data class ResultState(val movies: List<Movie>) : ViewState()
        class ErrorState(val error: String) : ViewState()
    }
}