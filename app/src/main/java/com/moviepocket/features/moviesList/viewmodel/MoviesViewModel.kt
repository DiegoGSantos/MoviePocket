package com.moviepocket.features.moviesList.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.moviepocket.features.moviesList.data.MovieRepository
import com.moviepocket.features.moviesList.model.Movie
import com.moviepocket.restclient.Service

/**
 * Created by diego.santos on 01/02/18.
 */
class MoviesViewModel
    constructor(
        var moviesLiveData: MutableLiveData<List<Movie>> = MutableLiveData(),
        val movieRepository: MovieRepository = MovieRepository()
    ): ViewModel() {

    fun listMovies() {

        movieRepository.getMovies { error, movies ->
            moviesLiveData.value = movies
        }
    }
}
