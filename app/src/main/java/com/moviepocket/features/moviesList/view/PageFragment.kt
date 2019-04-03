package com.moviepocket.features.moviesList.view

import android.app.ActivityOptions
import android.app.Dialog
import android.arch.lifecycle.Observer
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewCompat
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import com.moviepocket.R
import com.moviepocket.customViews.InfiniteScrollListener
import com.moviepocket.customViews.OnReleaseScreenListener
import com.moviepocket.databinding.FragmentPageBinding
import com.moviepocket.features.movieDetail.view.MovieDetailActivity
import com.moviepocket.features.moviesList.model.Movie
import com.moviepocket.features.moviesList.view.adapter.MoviesAdapter
import com.moviepocket.features.Event
import com.moviepocket.features.moviesList.viewmodel.MovieListScreenEvent
import com.moviepocket.features.moviesList.viewmodel.MovieListScreenState
import com.moviepocket.features.moviesList.viewmodel.MoviesViewModel
import com.moviepocket.interfaces.MoviesCLickListener
import com.moviepocket.manager.NetManager
import com.moviepocket.util.extensions.loadUrl
import com.moviepocket.util.extensions.reObserve
import kotlinx.android.synthetic.main.fragment_page.*
import kotlinx.android.synthetic.main.view_movie_preview.view.*
import org.koin.android.architecture.ext.viewModel
import org.koin.android.ext.android.inject

/**
 * Created by diegosantos on 12/17/17.
 */
class PageFragment : Fragment(), MoviesCLickListener, OnReleaseScreenListener {
    private var builder: Dialog? = null
    lateinit var listType: String
    private lateinit var moviesAdapter: MoviesAdapter
    private lateinit var binding: FragmentPageBinding

    private val moviesViewModel: MoviesViewModel by viewModel()
    private val netManager: NetManager by inject()

    companion object {
        const val LIST_TYPE = "LIST_TYPE"

        fun newInstance(listType: String): PageFragment {
            val args = Bundle()
            args.putString(LIST_TYPE, listType)
            val fragment = PageFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        loadObservers()
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_page, container, false)

        return binding.mainLayout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listType = arguments?.getString(LIST_TYPE) as String

        moviesAdapter = MoviesAdapter(this@PageFragment)
        setListeners()

        moviesViewModel.onViewCreated(listType)
    }

    override fun onMovieClick(movie: Movie, imageView: ImageView) {
        this.context?.let {
            moviesViewModel.onMovieClicked(movie, imageView)
        }
    }

    private fun openMovieDetail(movie: Movie, imageView: ImageView) {
        val intent = Intent(this.activity, MovieDetailActivity::class.java)
        intent.putExtra(Movie.MOVIE, movie)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivityForResult(intent, 101, ActivityOptions.makeSceneTransitionAnimation(
                    this.activity, imageView, ViewCompat.getTransitionName(imageView)).toBundle())
        } else {
            startActivityForResult(intent, 101)
        }
    }

    override fun onMovieLongClick(movie: Movie) {

        this.context?.let {
            moviesViewModel.onMovieLongClicked(movie)
        }
    }

    private fun openMoviePreview(movie: Movie) {
        moviesList.isLayoutFrozen = true

        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.view_movie_preview, null)

        view.movieTitle.text = movie.title
        view.imdbRate.text = movie.voteAverage
        view.mMovieCover.loadUrl(movie.getPosterUrl())

        builder = Dialog(context)
        builder?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        builder?.setContentView(view)
        builder?.show()

        (this.activity as MainActivity).showBlurView()
    }

    private fun onConnectivityError() {
        Toast.makeText(this.context, getString(R.string.connectivity_error), Toast.LENGTH_SHORT).show()
    }

    private fun hidePreview() {
        builder?.dismiss()
        moviesList.isLayoutFrozen = false
    }

    override fun onReleaseScreenListener() {
        hidePreview()
        (this.activity as MainActivity).hideBlurView()
    }

    private fun setListeners() {
        mainLayout.setOnRealeseListener(this)

        moviesList.apply {
            setHasFixedSize(true)

            adapter = moviesAdapter
            val gridLayoutManager = GridLayoutManager(this.context, 3)
            layoutManager = gridLayoutManager
            addOnScrollListener(InfiniteScrollListener({
                moviesViewModel.onNewPageRequested(listType)
            }, gridLayoutManager))
        }
    }

    private fun loadObservers() {
        moviesViewModel.moviesScreenState.reObserve(this, Observer<MovieListScreenState> { screenState ->
            screenState?.apply {
                when {
                    isStatusOk() -> updateList(screenState)
                    isDataNotAvailable() ->
                        showNoDataAvailableScreen()
                    isLoading() ->
                        showLoadingScreen()
                    isThereError() ->
                        showErrorScreen()
                }
            }
        })

        moviesViewModel.movieScreenEvent.reObserve(this, Observer<Event<MovieListScreenEvent>> { screenEffectEvent ->
            screenEffectEvent?.apply {
                val screenEffect = screenEffectEvent.getContentIfNotHandled()
                when(screenEffect) {
                    is MovieListScreenEvent.OpenMovieDetail ->
                        openMovieDetail(screenEffect.movie, screenEffect.imageView)
                    is MovieListScreenEvent.OpenMoviePreview ->
                        openMoviePreview(screenEffect.movie)
                    is MovieListScreenEvent.Error -> onConnectivityError()
                }
            }
        })
    }

    private fun updateList(screenState: MovieListScreenState) {
        binding.moviesList.visibility = VISIBLE
        binding.loadingView.visibility = GONE
        binding.errorView.visibility = GONE
        moviesAdapter.addMovies(screenState.movies, screenState.isThereMorePages)
    }

    private fun showErrorScreen() {
        binding.moviesList.visibility = GONE
        binding.loadingView.visibility = GONE
        binding.errorView.visibility = VISIBLE
        binding.errorView.setErrorMessage(getString(R.string.movie_list_error))
    }

    private fun showLoadingScreen() {
        binding.moviesList.visibility = GONE
        binding.loadingView.visibility = VISIBLE
        binding.errorView.visibility = GONE
    }

    private fun showNoDataAvailableScreen() {
        binding.moviesList.visibility = GONE
        binding.loadingView.visibility = GONE
        binding.errorView.visibility = VISIBLE
        binding.errorView.setErrorMessage(getString(R.string.movie_list_error))
    }
}
