package com.androidprojectbase.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.androidprojectbase.restclient.response.MovieListResponse
import com.moviepocket.model.Movie
import com.moviepocket.restclient.Service
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by diego.santos on 01/02/18.
 */
class MoviesViewModel
    constructor(
        val service: Service = Service.Factory.create(),
        var moviesLiveData: MutableLiveData<List<Movie>> = MutableLiveData()
    ): ViewModel() {

    fun listMovies() {
        val compositeDisposable = CompositeDisposable()

        compositeDisposable.add(
            service.listMovies()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe ({
                    result ->
                    moviesLiveData.value = result.results
                }, { error ->
                    error.printStackTrace()
                })
        )
    }
}
