package com.moviepocket.features.moviesList.data

import com.moviepocket.features.moviesList.model.Movie
import com.moviepocket.restclient.Service
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by diegosantos on 2/4/18.
 */
class MovieRemoteDataSource
    constructor(
        val service: Service = Service.Factory.create()
    ){

    fun getMovies(page: String, listType: String, callback:(error: Any?, result: List<Movie>, page: Int) -> Unit) {
        val compositeDisposable = CompositeDisposable()

        compositeDisposable.add(
            service.listMovies(listType, page)
                .observeOn(Schedulers.trampoline())
                .subscribeOn(Schedulers.trampoline())
                .subscribe ({
                    result ->
                    callback(null, result.results, result.totalPages)
                }, { error ->
                    error.printStackTrace()
                })
        )
    }
}