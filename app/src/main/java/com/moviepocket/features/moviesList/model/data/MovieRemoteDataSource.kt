package com.moviepocket.features.moviesList.model.data

import com.moviepocket.restclient.Service
import com.moviepocket.restclient.response.MovieListResponse
import io.reactivex.Observable

/**
 * Created by diegosantos on 2/4/18.
 */
class MovieRemoteDataSource(){

    suspend fun getMovies(page: String, listType: String): MovieListResponse {
        return Service.create().listMovies(listType, page)
    }
}