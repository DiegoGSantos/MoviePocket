package com.androidprojectbase.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import com.bumptech.glide.Glide
import com.moviepocket.R
import com.moviepocket.model.Movie
import kotlinx.android.synthetic.main.item_movie.view.*

/**
 * Created by diegosantos on 12/17/17.
 */
class MoviesAdapter(val context: Context, val movies: ArrayList<Movie>) : RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>(){

    private var itemClick: ((Movie) -> Unit)? = null
    
    override fun getItemCount(): Int = movies?.size ?: 0

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies.get(position)
        Glide.with(context)
                .load(movie.getPosterUrl())
                .placeholder(R.drawable.poster_placeholder)
                .fallback(R.drawable.poster_placeholder)
                .error(R.drawable.poster_placeholder)
                .into(holder.movieCover)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesAdapter.MovieViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return MovieViewHolder(layoutInflater.inflate(R.layout.item_movie, parent, false))
    }

    class MovieViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val movieCover = v.mMovieImage
    }
}