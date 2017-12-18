package com.androidprojectbase.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import com.moviepocket.R
import com.moviepocket.model.Movie

/**
 * Created by diegosantos on 12/17/17.
 */
class MoviesAdapter(val context: Context, var movies: ArrayList<Movie>) : RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>(){

    private var itemClick: ((Movie) -> Unit)? = null
    companion object {
        var mMovies = ArrayList<Movie>()
//        var mListener: MovieListViewModel.MoviesListener? = null
    }

    init {
        mMovies.addAll(movies)
//        mListener = listener
    }

    override fun getItemCount(): Int = mMovies?.size ?: 0

    override fun onBindViewHolder(holder: MovieViewHolder?, position: Int) {
//        val binding = holder?.binding
        val movie = mMovies?.get(position)

//        var viewModel = movie?.let { MoviesViewModel(it) }
//        binding?.viewModel = viewModel

        holder?.setClickListener(itemClick)

//        Glide.with(context).load(binding?.viewModel?.movie?.image_url).into(binding?.mMovieImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesAdapter.MovieViewHolder {
        val inflatedView = parent.inflate(R.layout.item_movie, false)

        return MovieViewHolder(inflatedView)
    }

    class MovieViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        var animation1 = AlphaAnimation(0.2f, 1.0f)
        var animation2 = AlphaAnimation(0.2f, 1.0f)

        fun setClickListener(callback: ((Movie) -> Unit)?){
//            binding.viewModel.clicks().subscribe {
//                callback?.invoke(binding.viewModel.movie)
//            }
        }

        init {

            animation1.setDuration(1500)
            animation1.setFillAfter(true)

            animation2.setDuration(1500)
            animation2.setFillAfter(true)
        }
    }

    fun setClickListener(itemClick: ((Movie) -> Unit)?) {
        this.itemClick = itemClick
    }
}