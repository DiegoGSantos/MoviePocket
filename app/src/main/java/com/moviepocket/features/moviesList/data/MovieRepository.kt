package com.moviepocket.features.moviesList.data

import com.moviepocket.features.movieDetail.data.MovieDetailRemoteDataSource
import com.moviepocket.restclient.response.MovieDetailResponse
import com.moviepocket.features.moviesList.model.Movie
import org.jetbrains.anko.doAsync

/**
 * Created by diegosantos on 2/3/18.
 */
class MovieRepository
    constructor(
        private val movieRemoteDataSource: MovieRemoteDataSource = MovieRemoteDataSource(),
        private val movieDetailRemoveDataSource: MovieDetailRemoteDataSource = MovieDetailRemoteDataSource()
    ){

    fun getMovies(page: Int, listType: String, callback:(error: Any?, movies: List<Movie>, totalPages: Int) -> Unit) {
        movieRemoteDataSource.getMovies(page, listType) { error, movies, totalPages ->
            callback(error, movies, totalPages)
            saveMovies(movies)
        }
    }

    fun getMovieDetail(movieId: String, callback: (error: Any?, movieDetail: MovieDetailResponse) -> Unit) {
       movieDetailRemoveDataSource.getMovieDetail(movieId) { error, movieDetail ->
           callback(error, movieDetail)
       }
    }

    private fun saveMovies(movies: List<Movie>) {

        doAsync {
            Movie.deleteAll()

            for (movie in movies) {
                movie.save()
            }
        }
    }
}