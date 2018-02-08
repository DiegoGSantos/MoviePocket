package com.moviepocket.interfaces

import com.moviepocket.model.Movie

/**
 * Created by diego.santos on 21/12/17.
 */
interface MoviesCLickListener {
    fun onMovieClick(movie: Movie)
    fun onMovieLongClick(movie: Movie)
}