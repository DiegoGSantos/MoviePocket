package com.moviepocket.features.moviesList.view

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import com.eightbitlab.supportrenderscriptblur.SupportRenderScriptBlur
import com.moviepocket.R
import com.moviepocket.features.moviesList.view.adapter.TabsPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        contentViewpager.adapter = TabsPagerAdapter(supportFragmentManager,
                this@MainActivity)

        slidingTabs.setupWithViewPager(contentViewpager)
        slidingTabs.setTabMode (TabLayout.MODE_SCROLLABLE);

        val windowBackground = window.decorView.background
        blurView.setupWith(root)
                .windowBackground(windowBackground)
                .blurAlgorithm(SupportRenderScriptBlur(this))
                .blurRadius(5f)
    }

    fun showBlurView() {
        blurView.visibility = VISIBLE
    }

    fun hideBlurView() {
        blurView.visibility = INVISIBLE
    }
}
