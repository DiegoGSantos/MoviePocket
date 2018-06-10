package com.moviepocket.features.moviesList.view

import android.app.ActivityOptions
import android.app.Dialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
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
import com.moviepocket.features.moviesList.data.MovieListTypes
import com.moviepocket.features.moviesList.model.Movie
import com.moviepocket.features.moviesList.view.adapter.MoviesAdapter
import com.moviepocket.features.moviesList.viewmodel.MovieListScreenState
import com.moviepocket.features.moviesList.viewmodel.MoviesViewModel
import com.moviepocket.features.moviesList.viewmodel.MoviesViewModelFactory
import com.moviepocket.interfaces.MoviesCLickListener
import com.moviepocket.manager.NetManager
import com.moviepocket.util.extensions.loadUrl
import com.moviepocket.util.extensions.reObserve
import kotlinx.android.synthetic.main.fragment_page.*
import kotlinx.android.synthetic.main.view_movie_preview.view.*
import org.koin.android.ext.android.inject


/**
 * Created by diegosantos on 12/17/17.
 */
class PageFragment : Fragment(), MoviesCLickListener, OnReleaseScreenListener {
    var builder: Dialog? = null
    lateinit var moviesAdapter: MoviesAdapter
    val viewModelFactory : MoviesViewModelFactory by inject()
    val netManager: NetManager by inject()
    lateinit var listType: String
    private val observer = Observer<MovieListScreenState> { screenState ->
        screenState?.let {
            when {
                screenState.isStatusOk() -> updateList(screenState.movies,
                        viewModel()?.isThereMoreItemsToLoad(listType) ?: true)
                screenState.isDataNotAvailable() ->
                    showNotDataAvailableScreen()
                screenState.isLoading() ->
                    showLoadingScreen()
                screenState.isThereError() ->
                    showErrorScreen()
            }
        }
    }

    private lateinit var binding: FragmentPageBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        loadObservers()
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_page, container, false)

        return binding.mainLayout
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
        this.context?.let {
            return ViewModelProviders.of(this, viewModelFactory).get(MoviesViewModel::class.java)
        }

        return null
    }

    private fun setListeners() {
        mainLayout.setOnRealeseListener(this)

        moviesList.apply {
            setHasFixedSize(true)

            adapter = moviesAdapter
            val gridLayoutManager = GridLayoutManager(this.context, 3)
            layoutManager = gridLayoutManager
            addOnScrollListener(InfiniteScrollListener({
                viewModel()?.listMovies(listType)
            }, gridLayoutManager))
        }
    }

    private fun loadObservers() {
        viewModel()?.moviesScreenState?.reObserve(this, observer)
    }

    private fun updateList(movies: List<Movie>, isThereMoreItemsToLoad: Boolean) {
        if (viewModel()?.getCurrentPage(listType) != 1) {
            binding.moviesList.visibility = VISIBLE
            binding.loadingView.visibility = GONE
            binding.errorView.visibility = GONE
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

    override fun onMovieClick(movie: Movie, imageView: ImageView) {

        this.context?.let {
            if (netManager.isConnectedToInternet){
                val intent = Intent(this.activity, MovieDetailActivity::class.java)
                intent.putExtra(Movie.MOVIE, movie)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivityForResult(intent, 101, ActivityOptions.makeSceneTransitionAnimation(
                            this.activity, imageView, ViewCompat.getTransitionName(imageView)).toBundle())
                } else {
                    startActivityForResult(intent, 101)
                }
            } else{
                onConnectivityError()
            }
        }
    }

    override fun onMovieLongClick(movie: Movie) {

        this.context?.let {
            if (netManager.isConnectedToInternet){
                moviesList.isLayoutFrozen = true;

                val layoutInflater = LayoutInflater.from(context)
                val view = layoutInflater.inflate(R.layout.view_movie_preview, null)

                view.movieTitle.text = movie.title
                view.imdbRate.text = movie.voteAverage
                view.mMovieCover.loadUrl(movie.getPosterUrl())

                builder = Dialog(context);
                builder?.requestWindowFeature(Window.FEATURE_NO_TITLE);
                builder?.setContentView(view);
                builder?.show()

                (this.activity as MainActivity).showBlurView()
            }else{
                onConnectivityError()
            }
        }
    }

    private fun onConnectivityError() {
        Toast.makeText(this.context, "Verifique sua conexão com a internet", Toast.LENGTH_SHORT).show()
    }

    fun hidePreview() {
        builder?.dismiss()
        moviesList.isLayoutFrozen = false;
    }

    override fun onReleaseScreenListener() {
        hidePreview()
        (this.activity as MainActivity).hideBlurView()
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

    private fun showNotDataAvailableScreen() {
        binding.moviesList.visibility = GONE
        binding.loadingView.visibility = GONE
        binding.errorView.visibility = VISIBLE
        binding.errorView.setErrorMessage(getString(R.string.movie_list_error))
    }
}
