package com.dhagz.githubusers

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.dhagz.githubusers.utils.NetworkStateReceiver
import com.google.android.material.snackbar.Snackbar

abstract class BaseActivity : AppCompatActivity() {

  private lateinit var networkStateReceiver: NetworkStateReceiver
  private lateinit var snackbar: Snackbar

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    networkStateReceiver = NetworkStateReceiver(getString(R.string.api_base_url))
    // Observe for network state
    networkStateReceiver.networkState.observe(this) { isInternetAvailable ->
      // Show UI for no internet available
      showNoInternetBanner(isInternetAvailable)
    }
    // Start listening to network state
    networkStateReceiver.start(this)
  }

  override fun setContentView(layoutResID: Int) {
    super.setContentView(layoutResID)
    initViews()
  }

  override fun setContentView(view: View?) {
    super.setContentView(view)
    initViews()
  }

  override fun setContentView(view: View?, params: ViewGroup.LayoutParams?) {
    super.setContentView(view, params)
    initViews()
  }

  open fun initViews() {
    // Initialize snackbar for no internet
    snackbar = Snackbar.make(getMainContainer(), R.string.no_internet, Snackbar.LENGTH_INDEFINITE)
  }

  override fun onDestroy() {
    super.onDestroy()
    // Stop listening to network state
    networkStateReceiver.stop(this)
  }

  abstract fun getMainContainer(): View

  open fun showNoInternetBanner(isInternetAvailable: Boolean) {
    if (isInternetAvailable) {
      snackbar.dismiss()
    } else {
      snackbar.show()
    }
  }

}