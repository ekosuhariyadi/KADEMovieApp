package com.codangcoding.kmovieapp.presentation.list

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.codangcoding.kmovieapp.R
import com.codangcoding.kmovieapp.presentation.detail.MovieDetailActivity
import com.codangcoding.kmovieapp.presentation.list.MovieListContract.ViewState.LoadingState
import com.codangcoding.kmovieapp.presentation.list.MovieListContract.ViewState.ResultState
import com.codangcoding.kmovieapp.ui.VerticalLinearLayoutOffsetItemDecoration
import kotlinx.android.synthetic.main.activity_movie_list.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get

class MovieListActivity : AppCompatActivity(), MovieListContract.View,
    SwipeRefreshLayout.OnRefreshListener {

    private var selectedMenuId = R.id.mn_popular

    private lateinit var adapter: MovieListAdapter

    lateinit var presenter: MovieListContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_list)

        initPresenter()
        initView()

        presenter.loadPopularMovies()
    }

    private fun initPresenter() {
        injector.invoke(this)
    }

    private fun initView() {
        swipeRefresh.setOnRefreshListener(this)

        adapter = MovieListAdapter {
            val intent = Intent(this, MovieDetailActivity::class.java)
                .putExtra(MovieDetailActivity.EXTRA_MOVIE, it)
            startActivity(intent)
        }
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        movieList.layoutManager = layoutManager
        movieList.adapter = adapter
        movieList.addItemDecoration(
            VerticalLinearLayoutOffsetItemDecoration(resources.getDimensionPixelOffset(R.dimen.list_offset))
        )
        ViewCompat.setNestedScrollingEnabled(movieList, false)

        GlobalScope.launch {
            for (viewState in presenter.viewStates()) {
                runOnUiThread {
                    renderState(viewState)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_movie_list, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mn_popular -> {
                selectedMenuId = item.itemId
                tv_header.text = resources.getString(R.string.popular_movies)
                presenter.loadPopularMovies()
                true
            }
            R.id.mn_now_playing -> {
                selectedMenuId = item.itemId
                tv_header.text = resources.getString(R.string.now_playing_movies)
                presenter.loadNowPlayingMovies()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun renderState(viewState: MovieListContract.ViewState) {
        when (viewState) {
            is LoadingState -> swipeRefresh.isRefreshing = true
            is ResultState -> {
                swipeRefresh.isRefreshing = false
                adapter.submitList(viewState.movies)
            }
            is MovieListContract.ViewState.ErrorState -> {
                swipeRefresh.isRefreshing = false
                Toast.makeText(this, viewState.error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onRefresh() {
        when (selectedMenuId) {
            R.id.mn_popular -> {
                presenter.loadPopularMovies()
            }
            R.id.mn_now_playing -> {
                presenter.loadNowPlayingMovies()
            }
        }
    }

    companion object {
        var injector = { activity: MovieListActivity ->
            activity.presenter = activity.get()
        }
    }
}