package com.moviepocket.util.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.design.widget.Snackbar
import android.view.View
import android.widget.Toast

/**
 * Created by diego.santos on 08/02/18.
 */
fun Context.showSnackBar(view: View, text: String) {
    Snackbar.make(view, text, Snackbar.LENGTH_LONG).show()
}

fun Context.showToast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_LONG).show()
}

inline fun <reified T : Activity> Activity.launchActivity(noinline init: Intent.() -> Unit = {}) {
    val intent = newIntent<T>(this)
    intent.init()
    startActivity(intent)
}

inline fun <reified T : Any> newIntent(context: Context): Intent = Intent(context, T::class.java)