package com.moviepocket.features.moviesList.view

import android.app.Dialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.View.GONE
import com.moviepocket.features.moviesList.view.adapter.MoviesAdapter
import com.moviepocket.R
import kotlinx.android.synthetic.main.fragment_page.*
import android.view.*
import com.moviepocket.customViews.OnReleaseScreenListener
import com.moviepocket.interfaces.MoviesCLickListener
import com.moviepocket.features.moviesList.viewmodel.MoviesViewModel
import com.bumptech.glide.Glide
import com.moviepocket.features.movieDetail.MovieDetailActivity
import com.moviepocket.features.moviesList.model.Movie
import com.moviepocket.util.extensions.launchActivity
import com.moviepocket.util.extensions.loadUrl
import kotlinx.android.synthetic.main.view_movie_preview.view.*
import pl.allegro.fogger.ui.dialog.DialogWithBlurredBackgroundLauncher

/**
 * Created by diegosantos on 12/17/17.
 */
class PageFragment : Fragment(), MoviesCLickListener, OnReleaseScreenListener {
    var builder: Dialog? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_page, container, false)
    }

    override fun onResume() {
        super.onResume()

        setListeners()
        loadObservers()
        viewModel()?.listMovies()
    }

    private fun viewModel(): MoviesViewModel? {
        return ViewModelProviders.of(this).get(MoviesViewModel::class.java)
    }

    private fun setListeners() {
        mainLayout.setOnRealeseListener(this)
    }

    private fun loadObservers() {
        viewModel()?.moviesLiveData?.observe(this, Observer<List<Movie>> { posts ->
            posts?.let {
                updateUi(posts)
            }
        })
    }

    private fun updateUi(movies: List<Movie>) {
        moviesList.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(this.context, 3)
            adapter = MoviesAdapter(this.context, movies, this@PageFragment)

            progress.visibility = GONE 
        }
    }

    companion object {
        val ARG_PAGE = "ARG_PAGE"

        fun newInstance(page: Int): PageFragment {
            val args = Bundle()
            args.putInt(ARG_PAGE, page)
            val fragment = PageFragment()
            fragment.setArguments(args)
            return fragment
        }
    }

    override fun onMovieClick(movie: Movie) {
        this.activity.launchActivity<MovieDetailActivity>()
    }

    override fun onMovieLongClick(movie: Movie) {

        moviesList.isLayoutFrozen = true;

        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.view_movie_preview, null)

        view.movieTitle.text = movie.originalTitle
        view.imdbRate.text = movie.voteAverage
        view.mMovieCover.loadUrl(movie.getPosterUrl())

        builder = Dialog(context);
        builder?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

        builder?.setContentView(view);

        val dialogWithBlurredBackgroundLauncher = DialogWithBlurredBackgroundLauncher(this.activity)
        dialogWithBlurredBackgroundLauncher.showDialog(builder)
    }

    fun hidePreview() {
        builder?.dismiss()
        moviesList.isLayoutFrozen = false;
    }

    override fun onReleaseScreenListener() {
        hidePreview()
    }
}