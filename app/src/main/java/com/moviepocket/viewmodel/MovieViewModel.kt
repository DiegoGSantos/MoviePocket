package com.moviepocket.viewmodel

import com.moviepocket.model.Movie
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * Created by diegosantos on 12/17/17.
 */
class MovieViewModel(val movie: Movie){
    private val clicks = PublishSubject.create<Unit>()

//    fun getDescription() = movie.description
//    fun getPrice() = String.format("%.2f", movie.price)
//    fun getQuantity() = movie.quantity.toString()

    fun onClick() {
        clicks.onNext(Unit)
    }

    fun clicks(): Observable<Unit> = clicks.hide()
}