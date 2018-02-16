package com.moviepocket.features.movieDetail.data

import com.moviepocket.restclient.response.MovieDetailResponse
import com.moviepocket.restclient.Service
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by diegosantos on 2/4/18.
 */
class MovieDetailRemoteDataSource
    constructor(
        val service: Service = Service.create()
    ){

    fun getMovieDetail(movieId: String, callback:(error: Any?, result: MovieDetailResponse) -> Unit) {
        val compositeDisposable = CompositeDisposable()

        compositeDisposable.add(
            service.getMovieDetail(movieId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe ({
                    result ->
                    callback(null, result)
                }, { error ->
                    error.printStackTrace()
                })
        )
    }
}