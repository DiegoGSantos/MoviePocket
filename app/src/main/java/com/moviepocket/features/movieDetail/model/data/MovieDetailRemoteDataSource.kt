package com.moviepocket.features.movieDetail.model.data

import com.moviepocket.restclient.response.MovieDetailResponse
import com.moviepocket.restclient.Service
import io.reactivex.Observable

/**
 * Created by diegosantos on 2/4/18.
 */
class MovieDetailRemoteDataSource() {

    fun getMovieDetail(movieId: String): Observable<MovieDetailResponse> {
        return Service.Factory.create().getMovieDetail(movieId)
    }
}