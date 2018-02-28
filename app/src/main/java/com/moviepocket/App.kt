package com.moviepocket

import com.activeandroid.ActiveAndroid

/**
 * Created by diegosantos on 2/4/18.
 */
class App : com.activeandroid.app.Application() {

    companion object {
//        val TAG: String = App::class.simpleName.toString()
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this.applicationContext
        ActiveAndroid.initialize(this)
    }
}