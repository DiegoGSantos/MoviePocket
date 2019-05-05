package com.moviepocket.features.movieDetail.model.data

import com.moviepocket.restclient.response.MovieDetailResponse
import io.reactivex.Observable

/**
 * Created by diego.santos on 07/03/18.
 */
class MovieDetailRepository(private val movieDetailRemoveDataSource: MovieDetailRemoteDataSource){
    fun getMovieDetail(movieId: String): Observable<MovieDetailResponse> {
        return movieDetailRemoveDataSource.getMovieDetail(movieId)
    }
}