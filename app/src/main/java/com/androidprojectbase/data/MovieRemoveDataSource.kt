package com.androidprojectbase.data

import android.arch.lifecycle.MutableLiveData
import com.moviepocket.model.Movie
import com.moviepocket.restclient.Service
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by diegosantos on 2/4/18.
 */
class MovieRemoveDataSource
    constructor(
        val service: Service = Service.Factory.create()
    ){

    fun getMovies(callback:(error: Any?, result: List<Movie>) -> Unit) {
        val compositeDisposable = CompositeDisposable()

        compositeDisposable.add(
            service.listMovies()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe ({
                    result ->
                    callback(null, result.results)
                }, { error ->
                    error.printStackTrace()
                })
        )
    }
}