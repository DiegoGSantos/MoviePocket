package com.moviepocket.util

import android.content.Context
import android.location.Location
import android.util.Log
import android.view.View
import com.google.gson.Gson
import java.util.regex.Pattern
import android.net.NetworkInfo
import android.net.ConnectivityManager

/**
 * Created by diegosantos on 5/23/17.
 */

fun listIsNotEmpty(list: List<*>?): Boolean {
    return list != null && list.isNotEmpty()
}

fun isObjectNotNull(anyObject: Any?) : Boolean{
    return anyObject != null
}

fun isStringValid(text: String?): Boolean {
    return text != null && !text.isEmpty()
}

fun isStringValidNumber(text: String): Boolean {
    return isStringValid(text) && android.text.TextUtils.isDigitsOnly(text)
}

fun isStringValidDouble(text: String): Boolean {
    val Digits = "(\\p{Digit}+)"
    val HexDigits = "(\\p{XDigit}+)"
    // an exponent is 'e' or 'E' followed by an optionally
    // signed decimal integer.
    val Exp = "[eE][+-]?" + Digits
    val fpRegex = "[\\x00-\\x20]*" + // Optional leading "whitespace"

            "[+-]?(" + // Optional sign character

            "NaN|" + // "NaN" string

            "Infinity|" + // "Infinity" string


            // A decimal floating-point string representing a finite positive
            // number without a leading sign has at most five basic pieces:
            // Digits . Digits ExponentPart FloatTypeSuffix
            //
            // Since this method allows integer-only strings as input
            // in addition to strings of floating-point literals, the
            // two sub-patterns below are simplifications of the grammar
            // productions from the Java Language Specification, 2nd
            // edition, section 3.10.2.

            // Digits ._opt Digits_opt ExponentPart_opt FloatTypeSuffix_opt
            "(((" + Digits + "(\\.)?(" + Digits + "?)(" + Exp + ")?)|" +

            // . Digits ExponentPart_opt FloatTypeSuffix_opt
            "(\\.(" + Digits + ")(" + Exp + ")?)|" +

            // Hexadecimal strings
            "((" +
            // 0[xX] HexDigits ._opt BinaryExponent FloatTypeSuffix_opt
            "(0[xX]" + HexDigits + "(\\.)?)|" +

            // 0[xX] HexDigits_opt . HexDigits BinaryExponent FloatTypeSuffix_opt
            "(0[xX]" + HexDigits + "?(\\.)" + HexDigits + ")" +

            ")[pP][+-]?" + Digits + "))" +
            "[fFdD]?))" +
            "[\\x00-\\x20]*"// Optional trailing "whitespace"

    return Pattern.matches(fpRegex, text)
}

fun isViewVisible(editText: View): Boolean {
    return editText.visibility == View.VISIBLE
}

fun getStringFromObject(anyObject: Any) : String {
    val gson = Gson()
    return gson.toJson(anyObject)
}

fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo = connectivityManager.activeNetworkInfo
    return activeNetworkInfo != null && activeNetworkInfo.isConnected
}