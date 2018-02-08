package com.moviepocket.data

import com.moviepocket.model.Movie
/**
 * Created by diegosantos on 2/3/18.
 */
class MovieRepository
    constructor(
        val movieRemoveDataSource:MovieRemoveDataSource = MovieRemoveDataSource()
    ){

    fun getMovies(callback:(error: Any?, movies: List<Movie>) -> Unit) {
        movieRemoveDataSource.getMovies {error, movies ->
            callback(error, movies)
            saveMovies(movies)
        }
    }

    fun saveMovies(movies: List<Movie>) {

        Movie.deleteAll()

        for (movie in movies) {
            movie.save()
        }

        val movieList = Movie.getAll()
//
        var i = ""
    }
}