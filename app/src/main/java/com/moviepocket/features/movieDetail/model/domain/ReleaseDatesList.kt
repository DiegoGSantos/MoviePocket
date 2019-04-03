package com.moviepocket.features.movieDetail.model.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by diegosantos on 2/18/18.
 */
class ReleaseDatesList(@Expose val results: List<ReleaseDateInfo>)

data class ReleaseDateInfo(@Expose @SerializedName("iso_3166_1") val country: String,
                       @Expose @SerializedName("release_dates") val releaseDates: List<ReleaseDate>)

data class ReleaseDate(@Expose @SerializedName("release_date") val releaseDate: String)