package com.moviepocket.restclient.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by diego.santos on 15/02/18.
 */
class MovieDetailResponse (@Expose val title: String = "",
                           @Expose @SerializedName("vote_average")
                           val voteAverage: String = "",
                           @Expose @SerializedName("poster_path")
                           val posterPath: String = "",
                           @Expose val overview: String = "",
                           @Expose @SerializedName("backdrop_path")
                           val backdropPath: String = "") {

    fun getPosterUrl(): String = "http://image.tmdb.org/t/p/w342$posterPath"

    fun getBackdropPathUrl(): String = "http://image.tmdb.org/t/p/w342$backdropPath"
}