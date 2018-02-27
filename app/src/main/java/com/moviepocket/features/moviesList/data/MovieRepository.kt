package com.moviepocket.features.moviesList.data

import android.content.Context
import com.moviepocket.App
import com.moviepocket.features.movieDetail.data.MovieDetailRemoteDataSource
import com.moviepocket.restclient.response.MovieDetailResponse
import com.moviepocket.features.moviesList.model.Movie
import com.moviepocket.manager.NetManager
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync

/**
 * Created by diegosantos on 2/3/18.
 */
class MovieRepository(private val netManager: NetManager) {

    private val movieRemoteDataSource: MovieRemoteDataSource = MovieRemoteDataSource()
    private val movieLocalDataSource: MovieLocalDataSource = MovieLocalDataSource()
    private val movieDetailRemoveDataSource: MovieDetailRemoteDataSource = MovieDetailRemoteDataSource()

    fun getMovies(page: Int, listType: String, callback:(error: Any?, movies: List<Movie>, totalPages: Int) -> Unit) {

        netManager.isConnectedToInternet?.let {
            if (it) {
                movieRemoteDataSource.getMovies(page, listType) { error, movies, totalPages ->
                    callback(error, movies, totalPages)
                    saveMovies(movies, listType)
                }
            } else {
                movieLocalDataSource.getMovies(page, listType) { error, movies, totalPages ->
                    callback(error, movies, totalPages)
                }
            }
        }
    }

    fun getMovieDetail(movieId: String, callback: (error: Any?, movieDetail: MovieDetailResponse) -> Unit) {
       movieDetailRemoveDataSource.getMovieDetail(movieId) { error, movieDetail ->
           callback(error, movieDetail)
       }
    }

    private fun saveMovies(movies: List<Movie>, listType: String) {

        doAsync {
            Movie.deleteAllFromType(listType)

            for (movie in movies) {
                movie.listType = listType
                movie.save()
            }
        }
    }
}