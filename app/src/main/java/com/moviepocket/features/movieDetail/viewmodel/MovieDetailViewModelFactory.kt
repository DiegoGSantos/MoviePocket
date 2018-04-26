package com.moviepocket.features.movieDetail.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.moviepocket.features.movieDetail.data.MovieDetailRepository
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by diego.santos on 27/02/18.
 */
class MovieDetailViewModelFactory(private val repository: MovieDetailRepository,
                                  val processScheduler: Scheduler = Schedulers.io(),
                                  val androidScheduler: Scheduler = AndroidSchedulers.mainThread())
    : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieDetailViewModel::class.java)) {
            return MovieDetailViewModel(repository, processScheduler, androidScheduler) as T
        }

        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}