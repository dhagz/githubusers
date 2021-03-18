package com.dhagz.githubusers.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

object KeyboardUtils {

  @JvmStatic
  fun hideKeyboard(activity: Activity) {
    activity.currentFocus?.let { view ->
      val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
      imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }
  }
}