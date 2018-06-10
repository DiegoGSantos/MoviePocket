package com.moviepocket.features

import com.moviepocket.features.moviesList.viewmodel.ScreenStatus

open class ScreenState(val status: Int, val message: String) {
    fun isStatusOk(): Boolean = status == ScreenStatus.OK.status

    fun isDataNotAvailable(): Boolean = status == ScreenStatus.NO_DATA_FOUND.status

    fun isLoading(): Boolean = status == ScreenStatus.LOADING.status

    fun isThereError(): Boolean = status == ScreenStatus.ERROR.status
}