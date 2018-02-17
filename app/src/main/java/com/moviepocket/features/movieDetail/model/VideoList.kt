package com.moviepocket.features.movieDetail.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by diegosantos on 2/17/18.
 */
class VideoList(@Expose
                val results: List<Video>)