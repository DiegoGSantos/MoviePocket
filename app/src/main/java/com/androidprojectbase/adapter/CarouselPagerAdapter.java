package com.androidprojectbase.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.androidprojectbase.fragment.SliderItemFragment;

import java.util.ArrayList;

public class CarouselPagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {

    public final static float BIG_SCALE = 1.0f;
    public final static float SMALL_SCALE = 0.7f;
    public final static float DIFF_SCALE = BIG_SCALE - SMALL_SCALE;
    private static final int FIRST_PAGE = 2;
    public static final int LOOPS = 1000;
    private Activity context;
    private FragmentManager fragmentManager;

    ArrayList<String> mTitles;

    public CarouselPagerAdapter(Activity context, FragmentManager fm, ArrayList<String> titles) {
        super(fm);
        this.fragmentManager = fm;
        this.context = context;
        this.mTitles = titles;
    }

    @Override
    public Fragment getItem(int position) {

        String title = "";

        position = position % mTitles.size();

        if (position < mTitles.size()){
            title = mTitles.get(position);
        }

        return SliderItemFragment.newInstance(context, title);
    }

    @Override
    public int getCount() {
//        int count = 0;
//        try {
//            count = mTitles.size() * LOOPS;
//        } catch (Exception e) {
//            // TODO: handle exception
//            e.printStackTrace();
//        }
//        return count;
        return mTitles.size() * LOOPS;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        try {
            if (positionOffset >= 0f && positionOffset <= 1f) {
//                CarouselLinearLayout cur = getRootView(position);
//                CarouselLinearLayout next = getRootView(position + 1);

//                cur.setScaleBoth(BIG_SCALE - DIFF_SCALE * positionOffset);
//                next.setScaleBoth(SMALL_SCALE + DIFF_SCALE * positionOffset);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

//    @SuppressWarnings("ConstantConditions")
//    private CarouselLinearLayout getRootView(int position) {
//        return (CarouselLinearLayout) fragmentManager.findFragmentByTag(this.getFragmentTag(position))
//                .getView().findViewById(R.id.root_container);
//    }

//    private String getFragmentTag(int position) {
//        return "android:switcher:" + context.pager.getId() + ":" + position;
//    }
}