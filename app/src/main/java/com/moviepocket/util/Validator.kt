package com.moviepocket.util

import android.text.TextUtils

/**
 * Created by diegosantos on 12/16/17.
 */
fun isValidEmail(target: CharSequence): Boolean {
    return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(removeSpacesAtTheEnd(target.toString())).matches()
}

fun removeSpacesAtTheEnd(input: String): String {
    var input = input
    input = input.replace("\\s+$".toRegex(), "")
    return input
}

fun validateFullName(name: String, acceptNumbers: Boolean): Boolean {

    val ONLY_LETTERS_REGEX = "^[ a-zA-Z ]*$"

    val result: Boolean
    val names = name.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    //        if (names.length < 2) {
    //        For now not validating full name, just one name is enough to be considered valid
    if (names.size < 1) {
        return false
    }
    return if (!names[0].replace(" ", "").isEmpty() && !names[names.size - 1].replace(" ", "").isEmpty()) {
        if (acceptNumbers) {
            true
        } else {
            if (names[0].matches(ONLY_LETTERS_REGEX.toRegex()) && names[names.size - 1].matches(ONLY_LETTERS_REGEX.toRegex())) {
                true
            } else {
                false
            }
        }

    } else {
        false
    }
}