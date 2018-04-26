package com.moviepocket.features.moviesList.viewmodel

/**
 * Created by diegosantos on 2/18/18.
 */
enum class ScreenStatus(val status: Int) {
    OK(200),
    LOADING(0),
    NO_DATA_FOUND(204),
    ERROR(400),
}