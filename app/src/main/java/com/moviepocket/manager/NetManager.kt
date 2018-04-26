package com.moviepocket.manager

import android.content.Context
import android.net.ConnectivityManager
import com.moviepocket.App

/**
 * Created by diegosantos on 2/3/18.
 */
class NetManager() {

    val isConnectedToInternet: Boolean
        get() {

            val conManager = App.appContext.getSystemService(Context.CONNECTIVITY_SERVICE)
                    as ConnectivityManager

            val ni = conManager.activeNetworkInfo
            return ni != null && ni.isConnected
        }
}