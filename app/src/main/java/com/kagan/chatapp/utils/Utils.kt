package com.kagan.chatapp.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.android.material.snackbar.Snackbar
import com.kagan.chatapp.R

object Utils {

    fun hideKeyboard(context: Context, view: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun showApiFailure(context: Context, view: View) {
        Snackbar.make(
            view,
            context.resources.getString(R.string.api_failure),
            Snackbar.LENGTH_SHORT
        )
            .show()
    }
}