package com.Videopocket.features.VideoDetail.view.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.moviepocket.R
import com.moviepocket.features.movieDetail.model.domain.Video
import com.moviepocket.interfaces.VideoCLickListener
import com.moviepocket.util.extensions.loadUrl
import kotlinx.android.synthetic.main.item_video.view.*

/**
 * Created by diegosantos on 2/17/18.
 */

class VideosAdapter(val context: Context, val videos: List<Video>, val listener: VideoCLickListener) : RecyclerView.Adapter<VideosAdapter.VideoViewHolder>(){

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val video = v.tag as Video
            listener.onVideoClick(video)
        }
    }

    override fun getItemCount(): Int = videos?.size ?: 0

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = videos.get(position)
        holder.videoCover.loadUrl(video.getVideoImageUrl())

        with (holder.container) {
            tag = video
            setOnClickListener(mOnClickListener)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideosAdapter.VideoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return VideoViewHolder(layoutInflater.inflate(R.layout.item_video, parent, false))
    }

    class VideoViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val videoCover = v.mVideoImage
        val container = v.container
    }
}