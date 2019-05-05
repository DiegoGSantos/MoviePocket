package com.moviepocket.features.moviesList.model.data

import com.moviepocket.restclient.Service
import com.moviepocket.restclient.response.MovieListResponse
import io.reactivex.Observable

/**
 * Created by diegosantos on 2/4/18.
 */
class MovieRemoteDataSource(){

    fun getMovies(page: String, listType: String): Observable<MovieListResponse> {
        return Service.Factory.create().listMovies(listType, page)
    }
}