package com.moviepocket.model

import com.google.gson.annotations.SerializedName

/**
 * Created by diegosantos on 12/16/17.
 */
class Movie(@SerializedName("poster_path") val posterPath: String,
            val id: Int,
            @SerializedName("original_title") val originalTitle: String,
            @SerializedName("vote_average") val voteAverage: String){

    fun getPosterUrl(): String = "http://image.tmdb.org/t/p/w342$posterPath"
}