package com.moviepocket.features.moviesList.view

import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.appcompat.app.AppCompatActivity
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import com.moviepocket.R
import com.moviepocket.features.moviesList.view.adapter.TabsPagerAdapter
import eightbitlab.com.blurview.RenderScriptBlur
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        contentViewpager.adapter = TabsPagerAdapter(supportFragmentManager,
                this@MainActivity)

        slidingTabs.setupWithViewPager(contentViewpager)
        slidingTabs.tabMode = TabLayout.MODE_SCROLLABLE

        val windowBackground = window.decorView.background
        blurView.setupWith(root)
                .setFrameClearDrawable(windowBackground)
                .setBlurAlgorithm(RenderScriptBlur(this))
                .setBlurRadius(5f)
    }

    fun showBlurView() {
        blurView.visibility = VISIBLE
    }

    fun hideBlurView() {
        blurView.visibility = INVISIBLE
    }
}
