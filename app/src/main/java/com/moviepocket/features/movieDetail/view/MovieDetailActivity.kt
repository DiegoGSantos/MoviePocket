package com.moviepocket.features.movieDetail.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.PorterDuff
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View.VISIBLE
import com.eightbitlab.supportrenderscriptblur.SupportRenderScriptBlur
import com.moviepocket.R
import com.moviepocket.util.extensions.loadUrl
import kotlinx.android.synthetic.main.activity_movie_detail.*
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.moviepocket.features.movieDetail.viewmodel.MovieDetailViewModel
import com.moviepocket.features.moviesList.model.Movie
import com.moviepocket.features.moviesList.viewmodel.MoviesViewModel
import com.moviepocket.restclient.response.MovieDetailResponse


class MovieDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        setStatusBar()

        loadObservers()

        setListeners()

        intent.extras.getString(Movie.ID).let {
            viewModel()?.getMovieDetail(it)
        }
    }

    private fun viewModel(): MovieDetailViewModel? {
        return ViewModelProviders.of(this).get(MovieDetailViewModel::class.java)
    }

    private fun loadObservers() {
        viewModel()?.movieDetailLiveData?.observe(this, Observer<MovieDetailResponse> { movieDetail ->
            movieDetail?.let {
                updateUi(it)
            }
        })
    }

    private fun setListeners() {
        backButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun updateUi(movieDetailResponse: MovieDetailResponse) {
        setMoviePosterBackground(movieDetailResponse)

        setStarsStyle(movieDetailResponse)

        movieTitle.text = movieDetailResponse.title
        moviePlot.text = movieDetailResponse.overview
        rating.text = getString(R.string.movieRating, movieDetailResponse.voteAverage)

        val realeaseDate = movieDetailResponse.releaseDate
        val date = realeaseDate.substring(8, 10) + "/" +
            realeaseDate.substring(5, 7) + "/" +
                realeaseDate.substring(0,4)
        releaseDate.text = getString(R.string.releaseDate, date)

        setGenres(movieDetailResponse)

        plot.visibility = VISIBLE
    }

    private fun setGenres(movieDetailResponse: MovieDetailResponse) {
        var genres = ""
        movieDetailResponse.genres.forEach{
            genres += it.name + ", "
        }

        movieGenres.text = genres.substring(0, genres.length - 2)
        movieGenres.isSelected = true
    }

    private fun setStarsStyle(movieDetail: MovieDetailResponse) {
        ratingBar.rating = movieDetail.voteAverage.toFloat() / 2
        val stars1 = ratingBar.progressDrawable as LayerDrawable
        stars1.getDrawable(2).setColorFilter(resources.getColor(R.color.yellow), PorterDuff.Mode.SRC_ATOP)
        stars1.getDrawable(0).setColorFilter(resources.getColor(R.color.yellow_dark), PorterDuff.Mode.SRC_ATOP)
    }

    private fun setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w = window
            w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }

    private fun setMoviePosterBackground(movie: MovieDetailResponse) {
        movieBg.loadUrl(movie.getPosterUrl())
        movieCover.loadUrl(movie.getPosterUrl())
//        moviePoster.loadUrl(movie.getBackdropPathUrl())

        Glide.with(this)
                .load(movie.getBackdropPathUrl())
                .centerCrop()
                .into(moviePoster)

        val windowBackground = window.decorView.background

        blurView.setupWith(root)
                .windowBackground(windowBackground)
                .blurAlgorithm(SupportRenderScriptBlur(this))
                .blurRadius(5f)
    }
}
