package com.moviepocket.features.moviesList.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.moviepocket.R
import com.moviepocket.features.moviesList.model.Movie
import com.moviepocket.interfaces.MoviesCLickListener
import com.moviepocket.util.adapter.ViewType
import com.moviepocket.util.adapter.ViewTypeDelegateAdapter
import com.moviepocket.util.extensions.inflate
import com.moviepocket.util.extensions.loadUrl
import kotlinx.android.synthetic.main.item_movie.view.*
import android.support.v4.content.ContextCompat.startActivity
import android.support.v4.view.ViewCompat
import android.support.v4.app.ActivityOptionsCompat
import android.content.Intent
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.moviepocket.customViews.RoundedCornersTransformation
import com.moviepocket.features.movieDetail.view.MovieDetailActivity


/**
 * Created by diegosantos on 2/18/18.
 */
class MoviesDelegateAdapter(val listener: MoviesCLickListener) : ViewTypeDelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return MovieViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
        holder as MovieViewHolder
        holder.bind(item as Movie)
    }

    inner class MovieViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
            parent.inflate(R.layout.item_movie)) {

        val movieCover = itemView.mMovieImage

        fun bind(movie: Movie) {

            Glide.with(itemView.context)
                    .load(movie.getPosterUrl())
                    .placeholder(R.drawable.poster_placeholder)
                    .fallback(R.drawable.poster_placeholder)
                    .error(R.drawable.poster_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .bitmapTransform(RoundedCornersTransformation(itemView.context,10, 2))
                    .into(movieCover)

            super.itemView.setOnLongClickListener{ listener.onMovieLongClick(movie); true}
            super.itemView.setOnClickListener { listener.onMovieClick(movie, itemView.mMovieImage)}
        }
    }
}