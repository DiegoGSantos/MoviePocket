package com.moviepocket.features.movieDetail.view

import android.arch.lifecycle.Observer
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.PorterDuff
import android.graphics.drawable.LayerDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View.*
import android.view.WindowManager
import com.Videopocket.features.VideoDetail.view.adapter.VideosAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.eightbitlab.supportrenderscriptblur.SupportRenderScriptBlur
import com.moviepocket.R
import com.moviepocket.customViews.RoundedCornersTransformation
import com.moviepocket.databinding.ActivityMovieDetailBinding
import com.moviepocket.features.movieDetail.model.domain.Video
import com.moviepocket.features.movieDetail.viewmodel.MovieDetailScreenState
import com.moviepocket.features.movieDetail.viewmodel.MovieDetailViewModel
import com.moviepocket.features.moviesList.model.domain.Movie
import com.moviepocket.interfaces.VideoCLickListener
import com.moviepocket.restclient.response.MovieDetailResponse
import com.moviepocket.util.extensions.loadUrl
import kotlinx.android.synthetic.main.activity_movie_detail.*
import org.koin.android.architecture.ext.viewModel


class MovieDetailActivity : AppCompatActivity(), VideoCLickListener {

    private lateinit var binding: ActivityMovieDetailBinding
    private val movieDetailViewModel: MovieDetailViewModel by viewModel()

    override fun onVideoClick(video: Video) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(video.getVideoUrl())))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportPostponeEnterTransition()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail)

        setStatusBar()

        loadObservers()

        setListeners()

        setStarsStyle()

        intent.extras.getParcelable<Movie>(Movie.MOVIE).let {
            setMoviePosterBackground(it)
            movieTitle.text = it.title
            movieDetailViewModel.getMovieDetail(it.movieId)
        }
    }

    private fun loadObservers() {
        movieDetailViewModel.movieDetailLiveData.observe(this, Observer<MovieDetailScreenState> { movieDetail ->
            movieDetail?.let {
                when {
                    it.isStatusOk() -> updateUi(it.movieDetail)
                    it.isLoading() -> binding.progress.visibility = VISIBLE
                    it.isThereError() -> showErrorScreen()
                }
            }
        })
    }

    private fun setListeners() {
        backButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun updateUi(movieDetailResponse: MovieDetailResponse?) {
        if (movieDetailResponse != null) {
            setTopCover(movieDetailResponse)

            movieGenres.isSelected = true

            setVideoList(movieDetailResponse)

            setPlot(movieDetailResponse)

            setReleaseDate(movieDetailResponse)

            setGenres(movieDetailResponse)

            setRating(movieDetailResponse)
        }

        binding.progress.visibility = GONE
    }

    private fun setVideoList(movieDetailResponse: MovieDetailResponse) {
        if(movieDetailResponse.videos.results.isNotEmpty()) {
            videosList.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
                adapter = VideosAdapter(this.context, movieDetailResponse.videos.results, this@MovieDetailActivity)
            }

            videos.visibility = VISIBLE
            videosList.visibility = VISIBLE
        }
    }

    private fun setStarsStyle() {
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

    private fun setMoviePosterBackground(movie: Movie) {
        movieBg.loadUrl(movie.getPosterUrl())
        setMovieCover(movie.getPosterUrl())

        val windowBackground = window.decorView.background

        blurView.setupWith(root)
                .windowBackground(windowBackground)
                .blurAlgorithm(SupportRenderScriptBlur(this))
                .blurRadius(5f)
    }

    private fun setTopCover(movie: MovieDetailResponse) {
        Glide.with(this)
                .load(movie.getBackdropPathUrl())
                .centerCrop()
                .into(moviePoster)
    }

    private fun setMovieCover(posterUrl: String) {
        Glide.with(this)
                .load(posterUrl)
                .dontAnimate()
                .placeholder(R.drawable.poster_placeholder)
                .fallback(R.drawable.poster_placeholder)
                .error(R.drawable.poster_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .bitmapTransform(RoundedCornersTransformation(this,10, 2))
                .listener(object : RequestListener<String, GlideDrawable> {
                    override fun onException(e: Exception, model: String, target: com.bumptech.glide.request.target.Target<GlideDrawable>, isFirstResource: Boolean): Boolean {
                        supportStartPostponedEnterTransition()
                        return false
                    }

                    override fun onResourceReady(resource: GlideDrawable, model: String, target: com.bumptech.glide.request.target.Target<GlideDrawable>, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                        supportStartPostponedEnterTransition()
                        return false
                    }
                })
                .into(movieCover)
    }

    private fun setPlot(movieDetail: MovieDetailResponse) {
        if (!movieDetail.overview.isEmpty()) {
            binding.moviePlot.text = movieDetail.overview
        } else {
            binding.moviePlot.text = getString(R.string.default_plot)
        }

        binding.moviePlot.visibility = VISIBLE
    }

    private fun setReleaseDate(movieDetailResponse: MovieDetailResponse) {
        var releaseDateText = movieDetailResponse.releaseDate

        movieDetailResponse.releaseDates.results.forEach {
            if (it.country == "BR") {
                releaseDateText = it.releaseDates.get(0).releaseDate
            }
        }

        val date = releaseDateText.substring(8, 10) + "/" +
                releaseDateText.substring(5, 7) + "/" +
                releaseDateText.substring(0, 4)
        binding.releaseDate.text = getString(R.string.releaseDate, date)
    }

    private fun setRating(movieDetail: MovieDetailResponse) {
        if (movieDetail.voteAverage.toFloat() != 0f) {
            ratingBar.rating = (movieDetail.voteAverage.toFloat() / 2)
            binding.rating.text = getString(R.string.movieRating, "%.1f".format(movieDetail.voteAverage.toFloat()))
            ratingBar.visibility = VISIBLE
        } else {
            ratingBar.visibility = INVISIBLE

        }
    }

    private fun setGenres(movieDetailResponse: MovieDetailResponse) {
        var genres = ""
        movieDetailResponse.genres.forEachIndexed { index, genre ->
            genres += genre.name + if (index == (movieDetailResponse.genres.size - 1)) "" else ", "
        }

        if (genres.isNotEmpty())  binding.movieGenres.text = genres
    }

    private fun showErrorScreen() {
        binding.progress.visibility = GONE
    }
}
