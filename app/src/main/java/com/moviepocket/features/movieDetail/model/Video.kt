package com.moviepocket.features.movieDetail.model

import com.google.gson.annotations.Expose

/**
 * Created by diegosantos on 2/17/18.
 */
class Video(@Expose val key: String = "") {
    fun getVideoUrl(): String = "https://www.youtube.com/watch?v=$key"

    fun getVideoImageUrl(): String = "https://img.youtube.com/vi/$key/mqdefault.jpg"
}