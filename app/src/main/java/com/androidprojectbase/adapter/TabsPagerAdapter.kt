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
    internal val PAGE_COUNT = 4
    private val tabTitles = arrayOf("EM EXIBIÇÃO", "PRÓXIMOS LANÇAMENTOS", "POPULARES", "MAIS VOTADOS")

    override fun getCount(): Int {
        return PAGE_COUNT
    }

    override fun getItem(position: Int): Fragment {

        if (position == 0){
            return PageFragment.newInstance(position)
        }else if (position == 1){
            return PageFragment.newInstance(position)
        }else if (position == 2){
            return PageFragment.newInstance(position)
        }
        return PageFragment.newInstance(position + 1)
    }

    override fun getPageTitle(position: Int): CharSequence {
        // Generate title based on item position
        return tabTitles[position]
    }
}