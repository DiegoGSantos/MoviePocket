package com.moviepocket.features.movieDetail.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.PorterDuff
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.WindowManager
import com.Videopocket.features.VideoDetail.view.adapter.VideosAdapter
import com.bumptech.glide.Glide
import com.eightbitlab.supportrenderscriptblur.SupportRenderScriptBlur
import com.moviepocket.R
import com.moviepocket.features.movieDetail.model.Video
import com.moviepocket.features.movieDetail.viewmodel.MovieDetailViewModel
import com.moviepocket.features.moviesList.model.Movie
import com.moviepocket.interfaces.VideoCLickListener
import com.moviepocket.restclient.response.MovieDetailResponse
import com.moviepocket.util.extensions.loadUrl
import android.content.Intent
import android.net.Uri
import android.view.View.*
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_movie_detail.*


class MovieDetailActivity : AppCompatActivity(), VideoCLickListener {
    override fun onVideoClick(video: Video) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(video.getVideoUrl())))
    }

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

        setRating(movieDetailResponse)

        movieTitle.text = movieDetailResponse.title

        setPlot(movieDetailResponse)

        setReleaseDate(movieDetailResponse)

        setGenres(movieDetailResponse)

        setVideoList(movieDetailResponse)

        progress.visibility = GONE
    }

    private fun setRating(movieDetailResponse: MovieDetailResponse) {
        if (movieDetailResponse.voteAverage.toFloat() != 0f) {
            setStarsStyle(movieDetailResponse)
            rating.text = getString(R.string.movieRating, "%.1f".format(movieDetailResponse.voteAverage.toFloat()))
        } else {
            rating.visibility = INVISIBLE
        }
    }

    private fun setPlot(movieDetailResponse: MovieDetailResponse) {
        if (!movieDetailResponse.overview.isEmpty()) {
            (moviePlot as TextView).text = movieDetailResponse.overview
//            moviePlot.text = movieDetailResponse.overview
            plot.visibility = VISIBLE
            moviePlot.visibility = VISIBLE
        }
    }

    private fun setReleaseDate(movieDetailResponse: MovieDetailResponse) {
        var releaseDateText = movieDetailResponse.releaseDate

        movieDetailResponse.releaseDates.results.forEach {
            if (it.country.equals("BR")) {
                releaseDateText = it.releaseDates.get(0).releaseDate
            }
        }

        val date = releaseDateText.substring(8, 10) + "/" +
                releaseDateText.substring(5, 7) + "/" +
                releaseDateText.substring(0, 4)
        releaseDate.text = getString(R.string.releaseDate, date)
    }

    private fun setVideoList(movieDetailResponse: MovieDetailResponse) {
        if(movieDetailResponse.videos.results.size > 0) {
            videosList.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
                adapter = VideosAdapter(this.context, movieDetailResponse.videos.results, this@MovieDetailActivity)
            }

            videos.visibility = VISIBLE
            videosList.visibility = VISIBLE
        }
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
