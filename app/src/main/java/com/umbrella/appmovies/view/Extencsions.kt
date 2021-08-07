package com.umbrella.appmovies.view

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.showSnackBar(text: String, actionText: String, action: (View) -> Unit) {
    val snackbar = Snackbar.make(this, text, Snackbar.LENGTH_INDEFINITE)
        .setAction(actionText, action)
    snackbar.anchorView = this
    snackbar.show()
}

fun View.show(): View {
    if (visibility != View.VISIBLE) {
        visibility = View.VISIBLE
    }
    return this
}

fun View.hide(): View {
    if (visibility != View.GONE) {
        visibility = View.GONE
    }
    return this
}