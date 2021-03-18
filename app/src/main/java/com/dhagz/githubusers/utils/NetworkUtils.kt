package com.dhagz.githubusers.utils

import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class NetworkUtils {
  companion object {

    /**
     * Ping the API server to check if the internet is really working or not.
     *
     * @return TRUE if the API is available. FALSE otherwise.
     */
    fun isApiAvailable(apiUrl: String?): Boolean {
      var success = false
      try {
        val url = URL(apiUrl)
        val connection = url.openConnection() as HttpURLConnection
        connection.connectTimeout = 10000
        connection.connect()
        success = connection.responseCode == 200
      } catch (e: IOException) {
        e.printStackTrace()
      }
      return success
    }
  }
}