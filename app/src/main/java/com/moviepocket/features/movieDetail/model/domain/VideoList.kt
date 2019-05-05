package com.moviepocket.features.movieDetail.model.domain

import com.google.gson.annotations.Expose

/**
 * Created by diegosantos on 2/17/18.
 */
class VideoList(@Expose
                val results: List<Video>)