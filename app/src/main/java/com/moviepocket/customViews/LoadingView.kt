package com.moviepocket.customViews

import android.content.Context
import android.util.AttributeSet
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import com.moviepocket.R
import kotlinx.android.synthetic.main.loading_view.view.*

/**
 * Created by diego.santos on 19/03/18.
 */
class LoadingView : LinearLayout {

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView()
    }

    fun initView(){
        inflate(getContext(), R.layout.loading_view, this);
    }
}