package com.moviepocket.util

import android.content.Context
import android.graphics.Typeface
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import java.util.*

/**
 * Created by diegosantos on 12/16/17.
 */
object FontUtils {
    val STYLE_BOLD = "bold"
    val STYLE_LIGHT = "light"
    val STYLE_SEMI_BOLD = "semibold"
    val STYLE_REGULAR = "regular"
    val STYLE_LINE_AWESOME = "line_awesome"
    val STYLE_AWESOME = "awesome"

    /* cache for loaded Open Sans typefaces*/
    private val TYPEFACE_CACHE = EnumMap<FontType, Typeface>(FontType::class.java)

    fun getFontFamily(style: String): FontFamily {
        when (style) {
            STYLE_REGULAR -> return FontFamily.OPENSANS
            STYLE_BOLD -> return FontFamily.OPENSANS_BOLD
            STYLE_LIGHT -> return FontFamily.OPENSANS_LIGHT
            STYLE_SEMI_BOLD -> return FontFamily.OPENSANS_SEMIBOLD
            STYLE_LINE_AWESOME -> return FontFamily.LINE_AWESOME
            STYLE_AWESOME -> return FontFamily.AWESOME
            else -> return FontFamily.OPENSANS
        }
    }

    enum class FontFamily {
        OPENSANS,
        OPENSANS_LIGHT,
        OPENSANS_SEMIBOLD,
        OPENSANS_BOLD,
        LINE_AWESOME,
        AWESOME
    }

    enum class FontType private constructor(val path: String) {
        OPENSANS("fonts/OpenSans/OpenSans-Regular.ttf"),
        OPENSANS_LIGHT("fonts/OpenSans/OpenSans-Light.ttf"),
        OPENSANS_SEMIBOLD("fonts/OpenSans/OpenSans-Semibold.ttf"),
        OPENSANS_BOLD("fonts/OpenSans/OpenSans-Bold.ttf"),
        LINE_AWESOME("fonts/LineAwesome/line-awesome.ttf"),
        AWESOME("fonts/FontAwesome/fontawesome.ttf")
    }

    /**
     * Creates Open Sans typeface and puts it into cache
     */
    fun getOpenSansTypeface(context: Context, fontType: FontType): Typeface? {
        val fontPath = fontType.path

        if (!TYPEFACE_CACHE.containsKey(fontType)) {
            try {
                TYPEFACE_CACHE.put(fontType, Typeface.createFromAsset(context.assets, fontPath))
            } catch (e: Exception) {
                Log.e("FontUtils", e.toString())
            }

        }

        return TYPEFACE_CACHE[fontType]
    }

    /**
     * Gets OpenSans typeface according to passed typeface style settings.
     *
     *
     * Will get OpenSans-Bold for Typeface.RC_BOLD etc
     */
    private fun getOpenSansTypeface(context: Context, originalTypeface: Typeface?, fontFamily: FontFamily): Typeface? {
        var opensansFontType: FontType? = null

        if (originalTypeface == null) {
            if (fontFamily == FontFamily.OPENSANS) {
                opensansFontType = FontType.OPENSANS
            } else if (fontFamily == FontFamily.OPENSANS_SEMIBOLD) {
                opensansFontType = FontType.OPENSANS_SEMIBOLD
            } else if (fontFamily == FontFamily.OPENSANS_BOLD) {
                opensansFontType = FontType.OPENSANS_BOLD
            } else if (fontFamily == FontFamily.OPENSANS_LIGHT) {
                opensansFontType = FontType.OPENSANS_LIGHT
            } else if (fontFamily == FontFamily.LINE_AWESOME) {
                opensansFontType = FontType.LINE_AWESOME
            } else if (fontFamily == FontFamily.AWESOME) {
                opensansFontType = FontType.AWESOME
            }
        } else {
            val style = originalTypeface.style

            when (style) {
                Typeface.BOLD -> if (fontFamily == FontFamily.OPENSANS) {
                    opensansFontType = FontType.OPENSANS
                } else if (fontFamily == FontFamily.OPENSANS_SEMIBOLD) {
                    opensansFontType = FontType.OPENSANS_SEMIBOLD
                } else if (fontFamily == FontFamily.OPENSANS_BOLD) {
                    opensansFontType = FontType.OPENSANS_BOLD
                }

                Typeface.ITALIC -> if (fontFamily == FontFamily.OPENSANS_LIGHT) {
                    opensansFontType = FontType.OPENSANS_LIGHT
                }

                Typeface.NORMAL -> if (fontFamily == FontFamily.OPENSANS) {
                    opensansFontType = FontType.OPENSANS
                } else if (fontFamily == FontFamily.OPENSANS_SEMIBOLD) {
                    opensansFontType = FontType.OPENSANS_SEMIBOLD
                } else if (fontFamily == FontFamily.OPENSANS_BOLD) {
                    opensansFontType = FontType.OPENSANS_BOLD
                }
            }
        }

        return if (opensansFontType == null) originalTypeface else getOpenSansTypeface(context, opensansFontType)
    }

    private fun getOpenSansFontType(fontFamily: FontFamily): FontType {
        var opensansFontType = FontType.OPENSANS // default

        if (fontFamily == FontFamily.OPENSANS_SEMIBOLD) {
            opensansFontType = FontType.OPENSANS_SEMIBOLD
        } else if (fontFamily == FontFamily.OPENSANS_BOLD) {
            opensansFontType = FontType.OPENSANS_BOLD
        } else if (fontFamily == FontFamily.OPENSANS_LIGHT) {
            opensansFontType = FontType.OPENSANS_LIGHT
        } else if (fontFamily == FontFamily.LINE_AWESOME) {
            opensansFontType = FontType.LINE_AWESOME
        } else if (fontFamily == FontFamily.AWESOME) {
            opensansFontType = FontType.AWESOME
        }

        return opensansFontType
    }

    /**
     * Walks ViewGroups, finds TextViews and applies Typefaces taking styling in consideration
     *
     * @param context - to reach assets
     * @param view    - root view to apply typeface to
     */
    fun setOpenSansFont(context: Context, view: View, fontFamily: FontFamily) {
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                setOpenSansFont(context, view.getChildAt(i), fontFamily)
            }
        } else if (view is TextView) {

            val fontType = getOpenSansFontType(fontFamily)
            val newTypeface = getOpenSansTypeface(context, fontType)

            if (newTypeface != null) {
                view.typeface = newTypeface
            }
        }
    }
}