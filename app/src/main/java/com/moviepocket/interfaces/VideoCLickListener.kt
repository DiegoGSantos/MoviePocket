package com.moviepocket.interfaces

import com.moviepocket.features.movieDetail.model.domain.Video

/**
 * Created by diego.santos on 21/12/17.
 */
interface VideoCLickListener {
    fun onVideoClick(video: Video)
}