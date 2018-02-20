package com.moviepocket.features.moviesList.data

import com.moviepocket.features.moviesList.model.Movie
import org.jetbrains.anko.doAsync

/**
 * Created by diegosantos on 2/4/18.
 */
class MovieLocalDataSource {

    fun getMovies(page: Int, listType: String, callback:(error: Any?, result: List<Movie>, page: Int) -> Unit) {
        doAsync {
            val movies = Movie.getAll()
            callback(null, movies, 1)
        }
    }
}