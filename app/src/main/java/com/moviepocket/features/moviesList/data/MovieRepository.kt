package com.moviepocket.features.moviesList.data

import com.moviepocket.features.movieDetail.data.MovieDetailRemoteDataSource
import com.moviepocket.features.moviesList.model.Movie
import com.moviepocket.manager.NetManager
import com.moviepocket.restclient.response.MovieDetailResponse
import com.moviepocket.restclient.response.MovieListResponse
import io.reactivex.Observable
import io.reactivex.Scheduler


/**
 * Created by diegosantos on 2/3/18.
 */
class MovieRepository(private val netManager: NetManager,
                      private val movieRemoteDataSource: MovieRemoteDataSource,
                      private val movieLocalDataSource: MovieLocalDataSource) {

    fun getMovies(page: String, listType: String): Observable<MovieListResponse>? {

        netManager.isConnectedToInternet?.let {
            if (it) {
                return movieRemoteDataSource.getMovies(page, listType)
            } else {
                return movieLocalDataSource.getMovies(page, listType)
            }
        }
    }

    fun saveMovies(movies: List<Movie>, listType: String, page: String) {
        movieLocalDataSource.saveMovies(movies, listType, page);
    }
}