package com.moviepocket.features.moviesList.model.data

import com.moviepocket.features.moviesList.model.domain.Movie
import com.moviepocket.manager.NetManager
import com.moviepocket.restclient.response.MovieListResponse
import io.reactivex.Observable


/**
 * Created by diegosantos on 2/3/18.
 */
class MovieRepository(private val netManager: NetManager,
                      private val movieRemoteDataSource: MovieRemoteDataSource,
                      private val movieLocalDataSource: MovieLocalDataSource) {

    fun getMovies(page: String, listType: String): Observable<MovieListResponse> {

        netManager.isConnectedToInternet.let {
            return if (it) {
                movieRemoteDataSource.getMovies(page, listType)
            } else {
                movieLocalDataSource.getMovies(page, listType)
            }
        }
    }

    fun saveMovies(movies: List<Movie>, listType: String, page: String) {
        movieLocalDataSource.saveMovies(movies, listType, page)
    }
}