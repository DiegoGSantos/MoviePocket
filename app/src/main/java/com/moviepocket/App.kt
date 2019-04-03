package com.moviepocket

import android.app.Application
import android.content.Context
import com.moviepocket.di.module
import com.moviepocket.features.moviesList.model.domain.Movie
import com.moviepocket.features.moviesList.model.domain.MyObjectBox

import io.objectbox.Box
import io.objectbox.BoxStore
import org.koin.android.ext.android.startKoin

/**
 * Created by diegosantos on 2/4/18.
 */
class App : Application() {

    companion object {
//        val TAG: String = App::class.simpleName.toString()
        lateinit var appContext: Context
        lateinit var myboxStore: BoxStore

        fun getBoxStore(): BoxStore{
            return myboxStore
        }

        fun getMovieBox(): Box<Movie> {
            return getBoxStore().boxFor(Movie::class.java)
        }
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this.applicationContext
        myboxStore = MyObjectBox.builder().androidContext(this.applicationContext).build()

        startKoin(this, listOf(module))
    }
}