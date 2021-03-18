package com.dhagz.githubusers._di

import android.content.Context
import com.dhagz.githubusers.R
import com.dhagz.githubusers._di.qualifiers.ApiUrlQualifier
import com.dhagz.githubusers._di.qualifiers.AppContextQualifier
import com.dhagz.githubusers.user.repo.remote.GitHubUsersService
import com.dhagz.githubusers.utils.NetworkStateReceiver
import dagger.Module
import dagger.Provides
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class NetworkModule {

  @Provides
  fun provideGitHubService(
      @ApiUrlQualifier baseUrl: String
  ): Retrofit {
    // Generic Requirements
    // REQUIREMENT 4.
    // All network calls must be queued and limited to 1 request at a time.

    val dispatcher = Dispatcher()
    dispatcher.maxRequests = 1

    val okHttpClient = OkHttpClient.Builder()
        .dispatcher(dispatcher)
        .build()


    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()
  }

  @Provides
  @ApiUrlQualifier
  fun apiBaseUrl(@AppContextQualifier context: Context): String {
    return context.getString(R.string.api_base_url)
  }

  @Provides
  fun networkStateReceiver(@ApiUrlQualifier apiUrl: String): NetworkStateReceiver {
    return NetworkStateReceiver(apiUrl)
  }

  @Provides
  fun gitHubUsersService(retrofit: Retrofit): GitHubUsersService {
    return retrofit.create(GitHubUsersService::class.java)
  }
}