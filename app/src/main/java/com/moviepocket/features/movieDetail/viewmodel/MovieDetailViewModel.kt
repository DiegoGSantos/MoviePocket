package com.moviepocket.features.movieDetail.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import com.moviepocket.App
import com.moviepocket.R
import com.moviepocket.features.ScreenState
import com.moviepocket.features.movieDetail.data.MovieDetailRepository
import com.moviepocket.features.moviesList.viewmodel.ScreenStatus
import com.moviepocket.restclient.response.MovieDetailResponse
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by diego.santos on 01/02/18.
 */
class MovieDetailViewModel(val movieRepository: MovieDetailRepository,
                           val processScheduler: Scheduler,
                           val androidScheduler: Scheduler): ViewModel() {
    var movieDetailLiveData: MutableLiveData<MovieDetailScreenState> = MutableLiveData()

    private var compositeDisposable = CompositeDisposable()

    fun getMovieDetail(movieId: String) {

        movieDetailLiveData.value = MovieDetailScreenState(ScreenStatus.LOADING.status, "", null)

        var disposable = movieRepository.getMovieDetail(movieId)
                .observeOn(androidScheduler)
                .subscribeOn(processScheduler)
                .subscribe({
                    movieDetail ->
                    movieDetailLiveData.value =
                            MovieDetailScreenState(ScreenStatus.OK.status, "", movieDetail)
                }, { error ->

                })

        compositeDisposable.add(disposable)
    }

    private fun unSubscribeFromObservable() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }

    private fun reset() {
        unSubscribeFromObservable()
        compositeDisposable.clear()
    }

    override fun onCleared() {
        reset()
        super.onCleared()
    }
}