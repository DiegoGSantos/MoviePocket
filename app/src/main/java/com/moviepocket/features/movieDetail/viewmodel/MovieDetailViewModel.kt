package com.moviepocket.features.movieDetail.viewmodel

import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import android.view.View
import com.moviepocket.App
import com.moviepocket.R
import com.moviepocket.features.movieDetail.data.MovieDetailRepository
import com.moviepocket.features.moviesList.data.MovieRepository
import com.moviepocket.features.moviesList.model.Movie
import com.moviepocket.restclient.response.MovieDetailResponse
import kotlinx.android.synthetic.main.activity_movie_detail.*

/**
 * Created by diego.santos on 01/02/18.
 */
class MovieDetailViewModel(val movieRepository: MovieDetailRepository): ViewModel() {
    var movieDetailLiveData: MutableLiveData<MovieDetailResponse> = MutableLiveData()
    val isLoading = ObservableField(false)
    val moviePlot = ObservableField("")
    val releaseDate = ObservableField("")
    val movieGenres = ObservableField("")
    val rating = ObservableField(0f)
    val ratingText = ObservableField("")
    val ratingVisible = ObservableField(true)

    fun getMovieDetail(movieId: String) {

        isLoading.set(true)

        movieRepository.getMovieDetail(movieId) { error, movieDetail ->
            movieDetailLiveData.value = movieDetail

            setPlot(movieDetail)

            setReleaseDate(movieDetail)

            setGenres(movieDetail)

            setRating(movieDetail)

            isLoading.set(false)
        }
    }

    private fun setRating(movieDetail: MovieDetailResponse) {
        if (movieDetail.voteAverage.toFloat() != 0f) {
            rating.set(movieDetail.voteAverage.toFloat() / 2)
            ratingText.set(App.appContext.getString(R.string.movieRating, "%.1f".format(movieDetail.voteAverage.toFloat())))
            ratingVisible.set(true)
        } else {
            ratingVisible.set(false)
        }
    }

    private fun setPlot(movieDetail: MovieDetailResponse) {
        if (!movieDetail.overview.isEmpty()) {
            moviePlot.set(movieDetail.overview)
        } else {
            moviePlot.set(App.appContext.getString(R.string.default_plot))
        }
    }

    private fun setReleaseDate(movieDetailResponse: MovieDetailResponse) {
        var releaseDateText = movieDetailResponse.releaseDate

        movieDetailResponse.releaseDates.results.forEach {
            if (it.country.equals("BR")) {
                releaseDateText = it.releaseDates.get(0).releaseDate
            }
        }

        val date = releaseDateText.substring(8, 10) + "/" +
                releaseDateText.substring(5, 7) + "/" +
                releaseDateText.substring(0, 4)
        releaseDate.set(App.appContext.getString(R.string.releaseDate, date))
    }

    private fun setGenres(movieDetailResponse: MovieDetailResponse) {
        var genres = ""
        movieDetailResponse.genres.forEach{
            genres += it.name + ", "
        }

        if (genres.isNotEmpty())  movieGenres.set(genres)
    }
}