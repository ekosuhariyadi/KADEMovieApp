package com.codangcoding.kmovieapp.presentation.list

import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.codangcoding.kmovieapp.BuildConfig
import com.codangcoding.kmovieapp.R
import com.codangcoding.kmovieapp.domain.data.MovieRepository
import com.codangcoding.kmovieapp.domain.data.MovieRepositoryImpl
import com.codangcoding.kmovieapp.external.data.MovieService
import com.codangcoding.kmovieapp.presentation.list.MovieListContract.ViewState.LoadingState
import com.codangcoding.kmovieapp.presentation.list.MovieListContract.ViewState.ResultState
import com.codangcoding.kmovieapp.ui.VerticalLinearLayoutOffsetItemDecoration
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import kotlinx.android.synthetic.main.activity_movie_list.*
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

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
        Companion.presenter?.let { this.presenter = it }
        if (this::presenter.isInitialized)
            return

        val objectMapper = ObjectMapper()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .registerKotlinModule()

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val originRequest = chain.request()
                val originUrl = originRequest.url()

                val newUrl = originUrl.newBuilder()
                    .addQueryParameter("api_key", BuildConfig.API_KEY)
                    .addQueryParameter("language", "en-US")
                    .build()
                val newRequest = originRequest.newBuilder()
                    .url(newUrl)
                    .build()

                chain.proceed(newRequest)
            }
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

        val movieService = retrofit.create(MovieService::class.java)
        val repository: MovieRepository = MovieRepositoryImpl(movieService)

        presenter = MovieListPresenter(repository)
    }

    private fun initView() {
        swipeRefresh.setOnRefreshListener(this)

        adapter = MovieListAdapter {
            Toast.makeText(this, it.title, Toast.LENGTH_SHORT).show()
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
                renderState(viewState)
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
        var presenter: MovieListContract.Presenter? = null
    }
}