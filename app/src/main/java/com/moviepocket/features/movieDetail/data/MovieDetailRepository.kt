package com.moviepocket.features.movieDetail.data

import com.moviepocket.restclient.response.MovieDetailResponse

/**
 * Created by diego.santos on 07/03/18.
 */
class MovieDetailRepository(private val movieDetailRemoveDataSource: MovieDetailRemoteDataSource){
    fun getMovieDetail(movieId: String, callback: (error: Any?, movieDetail: MovieDetailResponse) -> Unit) {
        movieDetailRemoveDataSource.getMovieDetail(movieId) { error, movieDetail ->
            callback(error, movieDetail)
        }
    }
}