package com.moviepocket.features.movieDetail.viewmodel

import com.moviepocket.features.ScreenState
import com.moviepocket.restclient.response.MovieDetailResponse

class MovieDetailScreenState(status: Int, message: String, val movieDetail: MovieDetailResponse?):
        ScreenState(status, message)