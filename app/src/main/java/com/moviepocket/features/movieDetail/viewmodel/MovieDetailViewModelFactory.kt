package com.moviepocket.features.movieDetail.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.moviepocket.features.moviesList.data.MovieRepository
import com.moviepocket.features.moviesList.viewmodel.MoviesViewModel

/**
 * Created by diego.santos on 27/02/18.
 */
class MovieDetailViewModelFactory(private val repository: MovieRepository)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieDetailViewModel::class.java)) {
            return MovieDetailViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}