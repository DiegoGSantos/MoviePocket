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
class MovieLocalDataSource(val processScheduler: Scheduler, val androidScheduler: Scheduler) {

    fun getMovies(page: String, listType: String, callback:(error: Any?, result: List<Movie>, page: String) -> Unit) {
        Observable.create(ObservableOnSubscribe<MovieListResponse> {
            emitter -> emitter.onNext(Movie.getAllFromType(listType, page))
        }).subscribeOn(processScheduler)
                .observeOn(androidScheduler)
                .subscribe{ result -> callback(null, result.results, result.totalPages.toString()) }
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

class LocalMovieListResponse (val totalOfPages: Int, val results: List<Movie>)