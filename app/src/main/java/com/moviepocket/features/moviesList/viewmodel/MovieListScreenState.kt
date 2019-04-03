package com.moviepocket.features.moviesList.viewmodel

import com.moviepocket.features.ScreenState
import com.moviepocket.features.moviesList.model.domain.Movie

/**
 * Created by diegosantos on 4/25/18.
 */
class MovieListScreenState (val currentPage: Int, val isThereMorePages: Boolean, status: Int, message: String, val movies: List<Movie>):
        ScreenState(status, message)