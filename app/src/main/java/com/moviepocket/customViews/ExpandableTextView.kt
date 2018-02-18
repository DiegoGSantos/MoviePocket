package com.moviepocket.customViews

import android.content.Context
import android.text.Html
import android.text.SpannableStringBuilder
import android.util.AttributeSet
import android.view.View
import android.widget.TextView

class ExpandableTextView : TextView {

    var originalText: CharSequence? = null
        private set
    private var trimmedText: CharSequence? = null
    private var bufferType: BufferType? = null
    private var trim = true
    private var trimLength: Int = 0
    private val displayableText: CharSequence?
        get() = if (trim) trimmedText else originalText

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        this.trimLength = 230

        setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                trim = !trim
                setText()
                requestFocusFromTouch()
            }
        })
    }

    private fun setText() {
        super.setText(Html.fromHtml(displayableText!!.toString()), bufferType)
    }

    override fun setText(text: CharSequence, type: BufferType) {
        originalText = text
        trimmedText = getTrimmedText(text)
        bufferType = type
        setText()
    }

    private fun getTrimmedText(text: CharSequence?): CharSequence? {
        return if (originalText != null && originalText!!.length > trimLength) {
            SpannableStringBuilder(originalText, 0, trimLength + 1).append(ELLIPSIS)
        } else {
            originalText
        }
    }

    companion object {
        private val ELLIPSIS = " <html><body><font color='#3088CD'>Continuar Lendo...</font></body></html>"
    }
}