package com.moviepocket.features.moviesList.view.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.moviepocket.features.moviesList.view.PageFragment
import androidx.fragment.app.FragmentPagerAdapter
import com.moviepocket.features.moviesList.model.data.MovieListTypes

/**
 * Created by diegosantos on 12/17/17.
 */
class TabsPagerAdapter(fm: FragmentManager, private val context: Context) : FragmentPagerAdapter(fm) {
    internal val PAGE_COUNT = 4
    private val tabTitles = arrayOf("EM EXIBIÇÃO", "PRÓXIMOS LANÇAMENTOS", "POPULARES", "MAIS VOTADOS")

    override fun getCount(): Int {
        return PAGE_COUNT
    }

    override fun getItem(position: Int): Fragment {

        if (position == 0){
            return PageFragment.newInstance(MovieListTypes.NOW_PLAYING.listType)
        }else if (position == 1){
            return PageFragment.newInstance(MovieListTypes.UPCOMING.listType)
        }else if (position == 2){
            return PageFragment.newInstance(MovieListTypes.POPULAR.listType)
        }else if (position == 3){
            return PageFragment.newInstance(MovieListTypes.TOP_RATED.listType)
        }
        return PageFragment.newInstance("")
    }

    override fun getPageTitle(position: Int): CharSequence {
        // Generate title based on item position
        return tabTitles[position]
    }
}