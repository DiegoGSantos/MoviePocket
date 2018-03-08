package com.moviepocket.features.moviesList.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.moviepocket.features.moviesList.data.MovieRepository
import io.reactivex.Scheduler

/**
 * Created by diego.santos on 27/02/18.
 */
class MoviesViewModelFactory(private val repository: MovieRepository, val processScheduler: Scheduler,
                             val androidScheduler: Scheduler)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MoviesViewModel::class.java)) {
            return MoviesViewModel(repository, processScheduler, androidScheduler) as T
        }

        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}