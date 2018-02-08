package com.moviepocket.features.moviesList.data

import com.moviepocket.features.moviesList.model.Movie
/**
 * Created by diegosantos on 2/3/18.
 */
class MovieRepository
    constructor(
        private val movieRemoveDataSource:MovieRemoveDataSource = MovieRemoveDataSource()
    ){

    fun getMovies(callback:(error: Any?, movies: List<Movie>) -> Unit) {
        movieRemoveDataSource.getMovies {error, movies ->
            callback(error, movies)
            saveMovies(movies)
        }
    }

    private fun saveMovies(movies: List<Movie>) {

        Movie.deleteAll()

        for (movie in movies) {
            movie.save()
        }
    }
}