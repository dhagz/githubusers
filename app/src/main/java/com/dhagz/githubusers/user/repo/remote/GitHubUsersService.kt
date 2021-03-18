package com.dhagz.githubusers.user.repo.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubUsersService {

  @GET("users/{login}")
  suspend fun fetchUser(@Path("login") login: String): Response<RemoteGitHubUser>

  @GET("users")
  suspend fun fetchUsers(@Query("since") sinceUserId: Long): Response<List<RemoteGitHubUser>>
}