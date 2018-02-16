package com.moviepocket.features.movieDetail.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.moviepocket.features.moviesList.data.MovieRepository
import com.moviepocket.features.moviesList.model.Movie
import com.moviepocket.restclient.response.MovieDetailResponse

/**
 * Created by diego.santos on 01/02/18.
 */
class MovieDetailViewModel
    constructor(
            var movieDetailLiveData: MutableLiveData<MovieDetailResponse> = MutableLiveData(),
            val movieRepository: MovieRepository = MovieRepository()
    ): ViewModel() {

    fun getMovieDetail(movieId: String) {

        movieRepository.getMovieDetail(movieId) { error, movieDetail ->
            movieDetailLiveData.value = movieDetail
        }
    }
}