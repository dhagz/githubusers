package com.dhagz.githubusers.utils

import java.text.DecimalFormat

object ConverterUtils {

  @JvmStatic
  fun numberFormat(value: Long): String {
    // Converts long to String.
    val formatter = DecimalFormat("#,###")
    return formatter.format(value)
  }
}