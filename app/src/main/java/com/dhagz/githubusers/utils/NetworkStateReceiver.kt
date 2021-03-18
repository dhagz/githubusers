package com.dhagz.githubusers.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject

class NetworkStateReceiver @Inject constructor(apiUrl: String) :
    ConnectivityManager.NetworkCallback() {

  private val TAG = "NetworkStateReceiver"

  private val apiUrl: String? = apiUrl
  private val networkRequest: NetworkRequest =
      NetworkRequest.Builder().addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
          .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
          .build()
  private val handler = Handler(Looper.getMainLooper())

  private val _networkState = MutableLiveData<Boolean>()
  val networkState: LiveData<Boolean> = _networkState

  fun start(context: Context) {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    connectivityManager.registerNetworkCallback(networkRequest, this)
  }

  fun stop(context: Context) {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    connectivityManager.unregisterNetworkCallback(this)
  }


  override fun onAvailable(network: Network) {
    super.onAvailable(network)
    val isApiAvailable: Boolean = NetworkUtils.isApiAvailable(apiUrl)
    Log.d(TAG, "#onAvailable() isApiAvailable=$isApiAvailable")
    handler.post {
      _networkState.value = true
    }
  }

  override fun onLost(network: Network) {
    super.onLost(network)
    Log.d(TAG, "#onLost()")
    handler.post {
      _networkState.value = false
    }
  }

  override fun onUnavailable() {
    super.onUnavailable()
    Log.d(TAG, "#onUnavailable()")
    handler.post {
      _networkState.value = false
    }
  }
}