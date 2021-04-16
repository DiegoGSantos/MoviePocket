package com.moviepocket

import android.app.Application
import android.content.Context
import com.moviepocket.di.mainModule
import com.moviepocket.features.moviesList.model.domain.Movie
import com.moviepocket.util.ObjectBox
import io.objectbox.Box
import org.koin.core.context.startKoin

/**
 * Created by diegosantos on 2/4/18.
 */
class App : Application() {

    companion object {
        lateinit var appContext: Context

        fun getMovieBox(): Box<Movie> {
            return ObjectBox.boxStore.boxFor(Movie::class.java)
        }
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this.applicationContext
        ObjectBox.init(this)

        startKoin {
            modules(mainModule)
        }
    }
}