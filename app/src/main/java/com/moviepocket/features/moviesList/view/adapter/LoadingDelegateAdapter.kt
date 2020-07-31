package com.moviepocket.features.moviesList.view.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import com.moviepocket.R
import com.moviepocket.util.adapter.ViewType
import com.moviepocket.util.adapter.ViewTypeDelegateAdapter
import com.moviepocket.util.extensions.inflate

class LoadingDelegateAdapter : ViewTypeDelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup) = LoadingViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
    }

    class LoadingViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
            parent.inflate(R.layout.item_movie_loading))
}