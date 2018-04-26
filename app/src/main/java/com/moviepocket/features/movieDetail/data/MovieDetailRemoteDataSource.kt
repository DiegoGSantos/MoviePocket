package com.moviepocket.features.movieDetail.data

import com.moviepocket.restclient.response.MovieDetailResponse
import com.moviepocket.restclient.Service
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by diegosantos on 2/4/18.
 */
class MovieDetailRemoteDataSource() {

    fun getMovieDetail(movieId: String): Observable<MovieDetailResponse> {
        return Service.Factory.create().getMovieDetail(movieId)
    }
}