package com.moviepocket.customViews

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.moviepocket.R
import kotlinx.android.synthetic.main.error_view.view.*

/**
 * Created by diegosantos on 4/25/18.
 */
class ErrorView : LinearLayout {

    constructor(context: Context) : super(context) {
        initView(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(attrs)
    }

    fun initView(attrs: AttributeSet?){
        inflate(getContext(), R.layout.error_view, this)

        if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.ErrorView)

            if (ta != null) {
                val message = ta.getString(R.styleable.ErrorView_errorMessage)

                if (!message.isNullOrEmpty()) errorMessage.text = message
                ta.recycle()
            }
        }
    }

    fun setErrorMessage(message: String) {
        errorMessage.text = message
    }
}