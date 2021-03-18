package com.dhagz.githubusers._di

import com.dhagz.githubusers.user.UserActivity
import com.dhagz.githubusers.userlist.UserListActivity
import dagger.Component

@Component(modules = [ContextModule::class, DatabaseModule::class, NetworkModule::class])
interface AppComponent {

  // This tells Dagger that MainActivity requests injection so the graph needs to
  // satisfy all the dependencies of the fields that LoginActivity is requesting.
  fun inject(activity: UserListActivity)

  fun inject(activity: UserActivity)
}