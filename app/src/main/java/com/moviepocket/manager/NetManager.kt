package com.moviepocket.manager

import android.content.Context
import android.net.ConnectivityManager

/**
 * Created by diegosantos on 2/3/18.
 */
class NetManager(var applicationContext: Context) {

    val isConnectedToInternet: Boolean?
        get() {

            val conManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE)
                    as ConnectivityManager

            val ni = conManager.activeNetworkInfo
            return ni != null && ni.isConnected
        }
}