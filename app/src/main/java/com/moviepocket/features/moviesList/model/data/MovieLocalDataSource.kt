package com.moviepocket.features.moviesList.model.data

import android.util.Log
import com.moviepocket.App
import com.moviepocket.features.moviesList.model.domain.Movie
import com.moviepocket.restclient.response.MovieListResponse
import com.moviepocket.util.ObjectBox
import io.reactivex.Observable

/**
 * Created by diegosantos on 2/4/18.
 */
class MovieLocalDataSource {

    fun getMovies(page: String, listType: String): MovieListResponse {
        return Movie.getAllFromType(listType, page)
    }

    fun saveMovies(movies: List<Movie>, listType: String, page: String) {

        Movie.deleteAllFromType(listType, page)

        ObjectBox.boxStore.runInTxAsync({
            for (movie in movies) {
                movie.page = page
                movie.listType = listType
            }

            App.getMovieBox().put(movies)
        }, {result, error ->  Log.d("DB", "Error: " + error?.message + " Result: " + result.toString())})
    }
}
