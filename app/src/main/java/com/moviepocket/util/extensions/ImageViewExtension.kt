package com.moviepocket.util.extensions

import android.support.v4.content.ContextCompat
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.moviepocket.R

/**
 * Created by diego.santos on 08/02/18.
 */
fun ImageView.loadUrl(url: String?) {
    Glide.with(context)
            .load(url)
            .placeholder(R.drawable.poster_placeholder)
            .fallback(R.drawable.poster_placeholder)
            .error(R.drawable.poster_placeholder)
            .dontAnimate()
            .into(this)
}