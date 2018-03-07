package com.moviepocket.features.moviesList.data

import com.moviepocket.features.movieDetail.data.MovieDetailRemoteDataSource
import com.moviepocket.features.moviesList.model.Movie
import com.moviepocket.manager.NetManager
import com.moviepocket.restclient.response.MovieDetailResponse


/**
 * Created by diegosantos on 2/3/18.
 */
class MovieRepository(private val netManager: NetManager,
                      private val movieRemoteDataSource: MovieRemoteDataSource,
                      private val movieLocalDataSource: MovieLocalDataSource) {

    fun getMovies(page: String, listType: String, callback: LoadMoviesCallback) {

        netManager.isConnectedToInternet?.let {
            if (it) {
                movieRemoteDataSource.getMovies(page, listType) { error, movies, totalPages ->
                    callback.onMoviesLoaded(error, movies, totalPages.toString())
                    movieLocalDataSource.saveMovies(movies, listType, page)
                }
            } else {
                movieLocalDataSource.getMovies(page, listType) { error, movies, totalPages ->
                    callback.onMoviesLoaded(error, movies, totalPages)
                }
            }
        }
    }

    interface LoadMoviesCallback {

        fun onMoviesLoaded(error: Any?, movies: List<Movie>, totalPages: String)

        fun onDataNotAvailable()
    }
}