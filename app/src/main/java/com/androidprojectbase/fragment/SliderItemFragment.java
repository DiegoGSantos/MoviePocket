package com.androidprojectbase.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.androidprojectbase.customViews.CarouselLinearLayout;
import com.moviepocket.R;
import com.moviepocket.customViews.CustomTextView;

public class SliderItemFragment extends Fragment {

    private static final String TITLE = "position";
    private static final String SCALE = "scale";

    private int screenWidth;
    private int screenHeight;

    public static Fragment newInstance(Activity context, String title) {
        Bundle b = new Bundle();
        b.putString(TITLE, title);
        return Fragment.instantiate(context, SliderItemFragment.class.getName(), b);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWidthAndHeight();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        final String title = this.getArguments().getString(TITLE);

        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.fragment_slider_item, container, false);

        CustomTextView textView = (CustomTextView) linearLayout.findViewById(R.id.text);
        CarouselLinearLayout root = (CarouselLinearLayout) linearLayout.findViewById(R.id.root_container);

        textView.setText(title);

        return linearLayout;
    }

    /**
     * Get device screen width and height
     */
    private void getWidthAndHeight() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenHeight = displaymetrics.heightPixels;
        screenWidth = displaymetrics.widthPixels;
    }
}
