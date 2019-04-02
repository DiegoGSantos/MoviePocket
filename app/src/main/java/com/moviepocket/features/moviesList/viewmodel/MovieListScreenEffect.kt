package com.moviepocket.features.moviesList.viewmodel

import android.widget.ImageView
import com.moviepocket.features.moviesList.model.Movie

sealed class MovieListScreenEffect {
    data class OpenMovieDetail(val movie: Movie, val imageView: ImageView) : MovieListScreenEffect()
    data class OpenMoviePreview(val movie: Movie) : MovieListScreenEffect()
    data class Error(val throwable: Throwable) : MovieListScreenEffect()
}