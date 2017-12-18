package com.moviepocket.customViews

import android.content.Context
import android.util.AttributeSet
import com.moviepocket.R
import com.moviepocket.util.FontUtils
import com.moviepocket.util.FontUtils.setOpenSansFont

/**
 * Created by diegosantos on 12/16/17.
 */
class CustomTextView : android.support.v7.widget.AppCompatTextView {

    internal var mContext: Context

    constructor(context: Context) : super(context) {

        mContext = context

        setTypefaceRoboto(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

        mContext = context

        setTypefaceRoboto(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {

        mContext = context

        setTypefaceRoboto(attrs)
    }

    internal fun setTypefaceRoboto(attrs: AttributeSet?) {
        var style: String? = null

        if (attrs != null) {
            val ta = mContext.obtainStyledAttributes(attrs, R.styleable.CustomTextView)
            style = ta.getString(R.styleable.CustomButton_customTypeface)
            ta.recycle()
            if (style != null && style !== "") {
                setOpenSansFont(mContext, this, FontUtils.getFontFamily(style))
            } else {
                setOpenSansFont(mContext, this, FontUtils.FontFamily.OPENSANS)
            }
        } else {
            setOpenSansFont(mContext, this, FontUtils.FontFamily.OPENSANS)
        }
    }

    internal fun setTypeface(style: String?) {
        if (style != null && style !== "") {
            setOpenSansFont(mContext, this, FontUtils.getFontFamily(style))
        }
    }
}