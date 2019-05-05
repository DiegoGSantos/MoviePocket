package com.moviepocket.restclient.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.moviepocket.features.moviesList.model.domain.Movie

/**
 * Created by diegosantos on 12/17/17.
 */
class MovieListResponse(@Expose @SerializedName("total_pages") val totalPages: Int, @Expose val results: ArrayList<Movie>)