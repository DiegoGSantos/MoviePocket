package com.moviepocket.features.moviesList.viewmodel

import android.widget.ImageView
import com.moviepocket.features.moviesList.model.domain.Movie

sealed class MovieListScreenEvent {
    data class OpenMovieDetail(val movie: Movie, val imageView: ImageView) : MovieListScreenEvent()
    data class OpenMoviePreview(val movie: Movie) : MovieListScreenEvent()
    data class Error(val throwable: Throwable) : MovieListScreenEvent()
}