package com.dhagz.githubusers

import android.app.Application
import com.dhagz.githubusers._di.AppComponent
import com.dhagz.githubusers._di.ContextModule
import com.dhagz.githubusers._di.DaggerAppComponent

class GitHubUsersApplication : Application() {

  val appComponent = DaggerAppComponent
      .builder()
      .contextModule(ContextModule(this))
      .build()
}