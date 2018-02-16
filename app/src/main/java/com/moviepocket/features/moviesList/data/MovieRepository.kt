package com.moviepocket.features.moviesList.data

import com.moviepocket.features.movieDetail.data.MovieDetailRemoteDataSource
import com.moviepocket.restclient.response.MovieDetailResponse
import com.moviepocket.features.moviesList.model.Movie
/**
 * Created by diegosantos on 2/3/18.
 */
class MovieRepository
    constructor(
        private val movieRemoteDataSource: MovieRemoteDataSource = MovieRemoteDataSource(),
        private val movieDetailRemoveDataSource: MovieDetailRemoteDataSource = MovieDetailRemoteDataSource()
    ){

    fun getMovies(callback:(error: Any?, movies: List<Movie>) -> Unit) {
        movieRemoteDataSource.getMovies { error, movies ->
            callback(error, movies)
            saveMovies(movies)
        }
    }

    fun getMovieDetail(movieId: String, callback: (error: Any?, movieDetail: MovieDetailResponse) -> Unit) {
       movieDetailRemoveDataSource.getMovieDetail(movieId) { error, movieDetail ->
           callback(error, movieDetail)
       }
    }

    private fun saveMovies(movies: List<Movie>) {

        Movie.deleteAll()

        for (movie in movies) {
            movie.save()
        }
    }
}