package com.moviepocket.features.movieDetail.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.database.DatabaseUtils
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
import android.widget.TextView
import com.Videopocket.features.VideoDetail.view.adapter.VideosAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.eightbitlab.supportrenderscriptblur.SupportRenderScriptBlur
import com.moviepocket.R
import com.moviepocket.customViews.RoundedCornersTransformation
import com.moviepocket.databinding.ActivityMovieDetailBinding
import com.moviepocket.di.Injector
import com.moviepocket.features.movieDetail.model.Video
import com.moviepocket.features.movieDetail.viewmodel.MovieDetailViewModel
import com.moviepocket.features.moviesList.model.Movie
import com.moviepocket.interfaces.VideoCLickListener
import com.moviepocket.restclient.response.MovieDetailResponse
import com.moviepocket.util.extensions.loadUrl
import kotlinx.android.synthetic.main.activity_movie_detail.*


class MovieDetailActivity : AppCompatActivity(), VideoCLickListener {

    private lateinit var binding: ActivityMovieDetailBinding

    override fun onVideoClick(video: Video) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(video.getVideoUrl())))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportPostponeEnterTransition()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail)
        binding.viewModel = viewModel()

        setStatusBar()

        loadObservers()

        setListeners()

        setStarsStyle()

        intent.extras.getParcelable<Movie>(Movie.MOVIE).let {
            setMoviePosterBackground(it)
            movieTitle.text = it.title
            viewModel()?.getMovieDetail(it.movieId ?: "")
        }
    }

    private fun viewModel(): MovieDetailViewModel? {
        return ViewModelProviders.of(this,
            Injector.provideMovieDetailViewModelFactory(this.applicationContext))
                .get(MovieDetailViewModel::class.java)
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
        setTopCover(movieDetailResponse)

        movieGenres.isSelected = true

        setVideoList(movieDetailResponse)
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
}
