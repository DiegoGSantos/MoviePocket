package com.moviepocket.features.moviesList.data

import android.util.Log
import com.moviepocket.App
import com.moviepocket.features.moviesList.model.Movie
import com.moviepocket.restclient.response.MovieListResponse
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync

/**
 * Created by diegosantos on 2/4/18.
 */
class MovieLocalDataSource() {

    fun getMovies(page: String, listType: String): Observable<MovieListResponse>? {
        return Observable.create(ObservableOnSubscribe<MovieListResponse> {
            emitter -> emitter.onNext(Movie.getAllFromType(listType, page))
        })
    }

    fun saveMovies(movies: List<Movie>, listType: String, page: String) {

        Movie.deleteAllFromType(listType, page)

        App.getBoxStore().runInTxAsync({
            for (movie in movies) {
                movie.page = page
                movie.listType = listType
            }

            App.getMovieBox().put(movies)
        }, {result, error ->  Log.d("DB", "Error: " + error?.message + " Result: " + result.toString())})
    }
}
