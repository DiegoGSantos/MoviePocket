package com.androidprojectbase.adapter

import android.app.Dialog
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.moviepocket.R
import com.moviepocket.model.Movie
import kotlinx.android.synthetic.main.item_movie.view.*
import android.databinding.adapters.ViewBindingAdapter.setOnLongClickListener
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window


/**
 * Created by diegosantos on 12/17/17.
 */
class MoviesAdapter(val context: Context, val movies: ArrayList<Movie>) : RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>(){

    private val mOnClickListener: View.OnClickListener
    private val mOnLongClickListener: OnLongClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val movie = v.tag as Movie
            Toast.makeText(context, "Movie click", Toast.LENGTH_LONG).show();
        }

        mOnLongClickListener = OnLongClickListener { it ->
            val movie = it.tag as Movie

            val layoutInflater = LayoutInflater.from(context)
            val view = layoutInflater.inflate(R.layout.item_movie, null)

            Glide.with(context)
                    .load(movie.getPosterUrl())
                    .placeholder(R.drawable.poster_placeholder)
                    .fallback(R.drawable.poster_placeholder)
                    .error(R.drawable.poster_placeholder)
                    .into(view.mMovieImage)

            val builder = Dialog(context);
            builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
            builder.getWindow().setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
            builder.setContentView(view);
            builder.show();

            true
        }
    }
    
    override fun getItemCount(): Int = movies?.size ?: 0

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies.get(position)
        Glide.with(context)
                .load(movie.getPosterUrl())
                .placeholder(R.drawable.poster_placeholder)
                .fallback(R.drawable.poster_placeholder)
                .error(R.drawable.poster_placeholder)
                .into(holder.movieCover)

        with (holder.container) {
            tag = movie
            setOnClickListener(mOnClickListener)
            setOnLongClickListener(mOnLongClickListener)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesAdapter.MovieViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return MovieViewHolder(layoutInflater.inflate(R.layout.item_movie, parent, false))
    }

    class MovieViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val movieCover = v.mMovieImage
        val container = v.container
    }
}