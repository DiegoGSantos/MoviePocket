package com.moviepocket.features.moviesList.model.data

import android.util.Log
import com.moviepocket.features.moviesList.model.domain.Movie
import com.moviepocket.manager.NetManager
import com.moviepocket.restclient.Result
import com.moviepocket.restclient.response.MovieListResponse
import retrofit2.HttpException
import java.net.UnknownHostException


/**
 * Created by diegosantos on 2/3/18.
 */
class MovieRepository(private val movieRemoteDataSource: MovieRemoteDataSource,
                      private val movieLocalDataSource: MovieLocalDataSource) {

    suspend fun getMovies(page: String, listType: String): Result<MovieListResponse> {
        return try {
            Result.success(
                movieRemoteDataSource.getMovies(page, listType)
            )
        } catch (e: UnknownHostException) {
            val moviesList = movieLocalDataSource.getMovies(page, listType)

            if (moviesList.results.size > 0) {
                Result.success(moviesList)
            } else {
                Result.failure(e)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun saveMovies(movies: List<Movie>, listType: String, page: String) {
        movieLocalDataSource.saveMovies(movies, listType, page)
    }
}