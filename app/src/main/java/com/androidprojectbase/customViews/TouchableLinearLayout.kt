package com.androidprojectbase.customViews

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.LinearLayout

/**
 * Created by diegosantos on 12/22/17.
 */
class TouchableLinearLayout : LinearLayout {

    internal var mListener: OnReleaseScreenListener? = null

    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {

        if (ev.action == MotionEvent.ACTION_UP || ev.action == MotionEvent.ACTION_CANCEL) {
            mListener?.onReleaseScreenListener()
        }

        return false
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            mListener?.onReleaseScreenListener()
            return true
        }
        return false
    }

    fun setOnRealeseListener(mListener: OnReleaseScreenListener) {
        this.mListener = mListener
    }
}

interface OnReleaseScreenListener {
    fun onReleaseScreenListener()
}