package com.moviepocket

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import com.androidprojectbase.adapter.CarouselPagerAdapter
import com.androidprojectbase.restclient.response.GenreListResponse
import com.androidprojectbase.restclient.response.MovieListResponse
import com.moviepocket.restclient.Service
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList
import android.support.v4.view.ViewPager
import com.androidprojectbase.adapter.TabsPagerAdapter
import kotlinx.android.synthetic.main.activity_main.view.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        contentViewpager.adapter = TabsPagerAdapter(supportFragmentManager,
                this@MainActivity)

        slidingTabs.setupWithViewPager(contentViewpager)
        slidingTabs.setTabMode (TabLayout.MODE_SCROLLABLE);
    }
}
