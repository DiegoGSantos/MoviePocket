package com.moviepocket.interfaces

import android.widget.ImageView
import com.moviepocket.features.moviesList.model.Movie

/**
 * Created by diego.santos on 21/12/17.
 */
interface MoviesCLickListener {
    fun onMovieClick(movie: Movie, imageView: ImageView)
    fun onMovieLongClick(movie: Movie)
}