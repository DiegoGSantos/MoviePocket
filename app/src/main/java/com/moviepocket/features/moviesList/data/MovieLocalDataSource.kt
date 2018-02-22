package com.moviepocket.features.moviesList.data

import com.moviepocket.features.moviesList.model.Movie
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync

/**
 * Created by diegosantos on 2/4/18.
 */
class MovieLocalDataSource {

    fun getMovies(page: Int, listType: String, callback:(error: Any?, result: List<Movie>, page: Int) -> Unit) {
        Observable.create(ObservableOnSubscribe<List<Movie>> {
            emitter -> emitter.onNext(Movie.getAllFromType(listType))
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{ result -> callback(null, result, 1) }
    }
}