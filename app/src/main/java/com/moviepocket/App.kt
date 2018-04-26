package com.moviepocket

import android.app.Application
import android.content.Context
import com.moviepocket.di.module
import com.moviepocket.features.moviesList.model.Movie
import com.moviepocket.features.moviesList.model.MyObjectBox
import io.objectbox.Box
import io.objectbox.BoxStore
import org.koin.android.ext.android.startKoin
import org.koin.dsl.module.Module

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
        myboxStore = MyObjectBox.builder().androidContext(appContext).build()

        startKoin(this, listOf(module))
    }
}