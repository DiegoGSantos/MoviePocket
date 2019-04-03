package com.moviepocket.features.movieDetail.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.moviepocket.features.movieDetail.data.MovieDetailRepository
import com.moviepocket.features.moviesList.viewmodel.ScreenStatus
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by diego.santos on 01/02/18.
 */
class MovieDetailViewModel(private val movieRepository: MovieDetailRepository,
                           private val processScheduler: Scheduler = Schedulers.io(),
                           private val androidScheduler: Scheduler = AndroidSchedulers.mainThread()): ViewModel() {
    val movieDetailLiveData: MutableLiveData<MovieDetailScreenState> = MutableLiveData()

    private var compositeDisposable = CompositeDisposable()

    fun getMovieDetail(movieId: String) {

        movieDetailLiveData.value = MovieDetailScreenState(ScreenStatus.LOADING.status, "", null)

        val disposable = movieRepository.getMovieDetail(movieId)
                .observeOn(androidScheduler)
                .subscribeOn(processScheduler)
                .subscribe({
                    movieDetail ->
                    movieDetailLiveData.value =
                            MovieDetailScreenState(ScreenStatus.OK.status, "", movieDetail)
                }, { error ->
                    movieDetailLiveData.value =
                            MovieDetailScreenState(ScreenStatus.ERROR.status, "", null)
                    error.printStackTrace()
                })

        compositeDisposable.add(disposable)
    }

    private fun unSubscribeFromObservable() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
        compositeDisposable.clear()
    }

    override fun onCleared() {
        unSubscribeFromObservable()
        super.onCleared()
    }
}