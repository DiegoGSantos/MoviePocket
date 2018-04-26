package com.moviepocket.features.moviesList.data

import com.moviepocket.features.moviesList.model.Movie
import com.moviepocket.restclient.Service
import com.moviepocket.restclient.response.MovieListResponse
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by diegosantos on 2/4/18.
 */
class MovieRemoteDataSource(){

    fun getMovies(page: String, listType: String): Observable<MovieListResponse> {
        return Service.Factory.create().listMovies(listType, page)
    }
}