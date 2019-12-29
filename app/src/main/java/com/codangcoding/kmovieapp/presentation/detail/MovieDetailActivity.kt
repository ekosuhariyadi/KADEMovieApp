package com.codangcoding.kmovieapp.presentation.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.codangcoding.kmovieapp.BuildConfig
import com.codangcoding.kmovieapp.GlideApp
import com.codangcoding.kmovieapp.R
import com.codangcoding.kmovieapp.domain.entity.Movie
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_movie_detail.*
import kotlin.math.abs

class MovieDetailActivity : AppCompatActivity(), AppBarLayout.OnOffsetChangedListener {

    private var ratingShown = true
    private var maxScrollSize = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        initView()
    }

    private fun initView() {
        toolbar.title = ""
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        appBar.addOnOffsetChangedListener(this)
        maxScrollSize = appBar.totalScrollRange

        collapsingToolbar.setExpandedTitleColor(
            ContextCompat.getColor(
                this,
                android.R.color.transparent
            )
        )
    }

    override fun onStart() {
        super.onStart()

        val movie = intent.getParcelableExtra<Movie>(EXTRA_MOVIE)
        renderMovie(movie)
    }

    private fun renderMovie(movie: Movie) {
        supportActionBar?.title = movie.title
        tv_title.text = movie.title
        tv_overview.text = movie.overview
        tv_release_date.text = movie.releaseDate
        tv_rating.text = "${movie.vote}"

        loadBackdrop(movie)
        loadPoster(movie)
    }

    private fun loadBackdrop(movie: Movie) {
        if (movie.backdropPath == null) {
            GlideApp.with(this)
                .load(R.mipmap.ic_launcher)
                .fitCenter()
                .into(iv_backdrop)
        } else {
            GlideApp.with(this)
                .load("${BuildConfig.IMAGE_URL}w780${movie.backdropPath}")
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .fitCenter()
                .into(iv_backdrop)
        }
    }

    private fun loadPoster(movie: Movie) {
        iv_poster.contentDescription = movie.title

        if (movie.posterPath == null) {
            GlideApp.with(this)
                .load(R.mipmap.ic_launcher_round)
                .fitCenter()
                .into(iv_poster)
        } else {
            GlideApp.with(this)
                .load("${BuildConfig.IMAGE_URL}w342${movie.posterPath}")
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round)
                .fitCenter()
                .into(iv_poster)
        }
    }

    override fun onOffsetChanged(p0: AppBarLayout?, verticalOffset: Int) {
        if (maxScrollSize == 0)
            maxScrollSize = appBar.totalScrollRange

        val percentage = abs(verticalOffset) * 100 / maxScrollSize
        if (percentage >= PERCENTAGE_TO_ANIMATE_RATING && ratingShown) {
            ratingShown = false

            ViewCompat.animate(rating_container).scaleX(0f).scaleY(0f).start()
        }

        if (percentage <= PERCENTAGE_TO_ANIMATE_RATING && !ratingShown) {
            ratingShown = true

            ViewCompat.animate(rating_container).scaleX(1f).scaleY(1f).start()
        }
    }

    companion object {
        private const val PERCENTAGE_TO_ANIMATE_RATING = 20

        const val EXTRA_MOVIE = "extra_movie"
    }
}