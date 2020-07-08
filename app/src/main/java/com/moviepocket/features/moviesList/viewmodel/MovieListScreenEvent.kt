package com.moviepocket.features.moviesList.viewmodel

import com.moviepocket.features.moviesList.model.domain.Movie

sealed class MovieListScreenEvent {
    data class OpenMovieDetail(val movie: Movie) : MovieListScreenEvent()
    data class OpenMoviePreview(val movie: Movie) : MovieListScreenEvent()
    data class Error(val throwable: Throwable) : MovieListScreenEvent()
}