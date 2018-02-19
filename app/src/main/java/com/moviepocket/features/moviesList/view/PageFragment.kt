package com.moviepocket.features.moviesList.view

import android.app.Dialog
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import com.moviepocket.R
import com.moviepocket.customViews.InfiniteScrollListener
import com.moviepocket.customViews.OnReleaseScreenListener
import com.moviepocket.features.movieDetail.view.MovieDetailActivity
import com.moviepocket.features.moviesList.data.MovieListTypes
import com.moviepocket.features.moviesList.model.Movie
import com.moviepocket.features.moviesList.view.adapter.MoviesAdapter
import com.moviepocket.features.moviesList.viewmodel.MoviesViewModel
import com.moviepocket.interfaces.MoviesCLickListener
import com.moviepocket.util.extensions.launchActivity
import com.moviepocket.util.extensions.loadUrl
import com.moviepocket.util.extensions.reObserve
import kotlinx.android.synthetic.main.fragment_page.*
import kotlinx.android.synthetic.main.view_movie_preview.view.*
import pl.allegro.fogger.ui.dialog.DialogWithBlurredBackgroundLauncher

/**
 * Created by diegosantos on 12/17/17.
 */
class PageFragment : Fragment(), MoviesCLickListener, OnReleaseScreenListener {
    var builder: Dialog? = null
    lateinit var moviesAdapter: MoviesAdapter
    lateinit var listType: String
    private val observer = Observer<List<Movie>> { posts ->
        posts?.let {
            updateUi(posts, viewModel()?.isThereMoreItemsToLoad(listType) ?: true)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        loadObservers()
        return inflater?.inflate(R.layout.fragment_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listType = arguments?.getString(ARG_PAGE) as String

        moviesAdapter = MoviesAdapter(this@PageFragment)
        resetInfiniteScroll()
        setListeners()

        viewModel()?.listMovies(listType)
    }

    private fun resetInfiniteScroll() {
        if (listType.equals(MovieListTypes.NOW_PLAYING.listType)) {
            viewModel()?.currentInTheaterPage = 1
            viewModel()?.isThereMoreInTheaterToLoad = true
        } else if (listType.equals(MovieListTypes.UPCOMING.listType)) {
            viewModel()?.currentUpcomingPage = 1
            viewModel()?.isThereMoreUpcomingToLoad = true
        }else if (listType.equals(MovieListTypes.POPULAR.listType)) {
            viewModel()?.currentPopularPage = 1
            viewModel()?.isThereMorePopularToLoad = true
        }else if (listType.equals(MovieListTypes.TOP_RATED.listType)) {
            viewModel()?.currentTopRatedPage = 1
            viewModel()?.isThereMoreTopRatedToLoad = true
        }
    }

    private fun viewModel(): MoviesViewModel? {
        return ViewModelProviders.of(this).get(MoviesViewModel::class.java)
    }

    private fun setListeners() {
        mainLayout.setOnRealeseListener(this)

        moviesList.apply {
            setHasFixedSize(true)

            adapter = moviesAdapter
            val gridLayoutManager = GridLayoutManager(this.context, 3)
            layoutManager = gridLayoutManager
            addOnScrollListener(InfiniteScrollListener({ viewModel()?.listMovies(listType) }, gridLayoutManager))

            progress.visibility = GONE
        }
    }

    private fun loadObservers() {
        viewModel()?.moviesLiveData?.reObserve(this, observer)
    }

    private fun updateUi(movies: List<Movie>, isThereMoreItemsToLoad: Boolean) {
        if (viewModel()?.getCurrentPage(listType) != 1) {
            moviesAdapter.addMovies(movies, isThereMoreItemsToLoad)
        }
    }

    companion object {
        val ARG_PAGE = "ARG_PAGE"

        fun newInstance(listType: String): PageFragment {
            val args = Bundle()
            args.putString(ARG_PAGE, listType)
            val fragment = PageFragment()
            fragment.setArguments(args)
            return fragment
        }
    }

    override fun onMovieClick(movie: Movie) {
        this.activity?.launchActivity<MovieDetailActivity>{
            putExtra(Movie.ID, movie.movieId)
        }
    }

    override fun onMovieLongClick(movie: Movie) {

        moviesList.isLayoutFrozen = true;

        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.view_movie_preview, null)

        view.movieTitle.text = movie.title
        view.imdbRate.text = movie.voteAverage
        view.mMovieCover.loadUrl(movie.getPosterUrl())

        builder = Dialog(context);
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
