package com.moviepocket.features.movieDetail

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import com.eightbitlab.supportrenderscriptblur.SupportRenderScriptBlur
import com.moviepocket.R
import com.moviepocket.util.extensions.loadUrl
import kotlinx.android.synthetic.main.activity_movie_detail.*
import android.view.WindowManager



class MovieDetailActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        movieBg.loadUrl("http://t0.gstatic.com/images?q=tbn:ANd9GcQiog19jyNYJ3uTY5pPNBCaWcS-mfJ8HLBLd58sGwB-1FObtFYX")
        movieCover.loadUrl("http://t0.gstatic.com/images?q=tbn:ANd9GcQiog19jyNYJ3uTY5pPNBCaWcS-mfJ8HLBLd58sGwB-1FObtFYX")

        val windowBackground = window.decorView.background

        blurView.setupWith(root)
            .windowBackground(windowBackground)
            .blurAlgorithm(SupportRenderScriptBlur(this))
            .blurRadius(5f)

        val stars = ratingBar.progressDrawable as LayerDrawable
        stars.getDrawable(2).setColorFilter(resources.getColor(R.color.yellow), PorterDuff.Mode.SRC_ATOP)
        stars.getDrawable(0).setColorFilter(resources.getColor(R.color.yellow_dark), PorterDuff.Mode.SRC_ATOP)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w = window
//            w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }
}
