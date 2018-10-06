package com.codangcoding.kmovieapp.presentation.list

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.codangcoding.kmovieapp.BuildConfig
import com.codangcoding.kmovieapp.GlideApp
import com.codangcoding.kmovieapp.R
import com.codangcoding.kmovieapp.domain.entity.Movie
import kotlinx.android.synthetic.main.list_item_movie.view.*

class MovieListAdapter(
    private val clickListener: (Movie) -> Unit
) : ListAdapter<Movie, MovieListAdapter.ViewHolder>(MovieDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val rootView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_movie, parent, false)

        return ViewHolder(rootView).apply {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    clickListener(getItem(position))
                }
            }
        }
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(getItem(position))
    }

    class ViewHolder(rootView: View) : RecyclerView.ViewHolder(rootView) {

        private val poster: ImageView = itemView.iv_poster
        private val title: TextView = itemView.tv_title
        private val overview: TextView = itemView.tv_overview
        private val releaseDate: TextView = itemView.tv_release_date

        fun bind(movie: Movie) {
            title.text = movie.title
            overview.text = movie.overview
            releaseDate.text = movie.releaseDate

            loadPoster(movie)
        }

        private fun loadPoster(movie: Movie) {
            poster.contentDescription = movie.title
            if (movie.posterPath == null)
                GlideApp.with(itemView.context)
                    .load(R.mipmap.ic_launcher_round)
                    .fitCenter()
                    .into(poster)
            else
                GlideApp.with(itemView.context)
                    .load("${BuildConfig.IMAGE_URL}w185${movie.posterPath}")
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher)
                    .fitCenter()
                    .into(poster)
        }
    }

    class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {

        override fun areItemsTheSame(movie: Movie, other: Movie): Boolean =
            movie == other

        override fun areContentsTheSame(movie: Movie, other: Movie): Boolean =
            movie == other
    }
}