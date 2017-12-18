package com.androidprojectbase.adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.androidprojectbase.fragment.PageFragment
import android.support.v4.app.FragmentPagerAdapter



/**
 * Created by diegosantos on 12/17/17.
 */
class TabsPagerAdapter(fm: FragmentManager, private val context: Context) : FragmentPagerAdapter(fm) {
    internal val PAGE_COUNT = 3
    private val tabTitles = arrayOf("Tab1", "Tab2", "Tab3")

    override fun getCount(): Int {
        return PAGE_COUNT
    }

    override fun getItem(position: Int): Fragment {
        return PageFragment.newInstance(position + 1)
    }

    override fun getPageTitle(position: Int): CharSequence {
        // Generate title based on item position
        return tabTitles[position]
    }
}