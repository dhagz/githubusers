package com.dhagz.githubusers.user.repo

import android.util.Log
import androidx.lifecycle.LiveData
import com.dhagz.githubusers._remote.ApiCallException
import com.dhagz.githubusers.user.repo.local.GitHubUser
import com.dhagz.githubusers.user.repo.local.GitHubUserDao
import com.dhagz.githubusers.user.repo.local.UserWithNote
import com.dhagz.githubusers.user.repo.remote.GitHubUsersService
import java.util.*
import javax.inject.Inject


class GitHubUsersRepo @Inject constructor(
  private val localDataSource: GitHubUserDao,
  private val remoteDataSource: GitHubUsersService
) {

  /**
   * Fetch the GitHub Users.
   *
   * @param sinceUserId The user ID of the last user of the previous list. If it is the first page set to 0
   */
  suspend fun fetchGitHubUsers(sinceUserId: Long = 0) {
    // GitHub users
    // REQUIREMENT 4
    // When there is data available (saved in the database) from
    // previous launches, that data should be displayed first, then (in parallel) new data
    // should be fetched from the backend.

    Log.d("GitHubUsersRepo", "#fetchGitHubUsers()")
    // Fetch data from remote data source
    val response = remoteDataSource.fetchUsers(sinceUserId)
    if (response.isSuccessful) {
      val remoteGitHubUsers = response.body()!!
      if (remoteGitHubUsers.isNotEmpty()) {
        val localGitHubUsers = LinkedList<GitHubUser>()
        // Transform data
        for (data in remoteGitHubUsers) {
          localGitHubUsers.add(data.toLocalDataModel())
        }
        // Insert only new data
        val res = localDataSource.insertAll(localGitHubUsers)
        Log.d("GitHubUsersRepo", "#fetchGitHubUsers() res=${res.size}")
      }
    }
  }

  /**
   * Fetch the GitHub Users stored in the local database.
   */
  fun fetchLocalGitHubUsers(): LiveData<List<UserWithNote>> {
    return localDataSource.fetchAll()
  }


  /**
   * Fetch the GitHub User by ID
   *
   * @param login The user login username of the last user of the previous list. If it is the first page set to 0
   */
  suspend fun fetchGitHubUser(login: String) {
    // GitHub users
    // REQUIREMENT 4
    // When there is data available (saved in the database) from
    // previous launches, that data should be displayed first, then (in parallel) new data
    // should be fetched from the backend.

    Log.d("GitHubUsersRepo", "#fetchGitHubUser() login: $login")
    // Fetch data from remote data source
    val response = remoteDataSource.fetchUser(login)
    if (response.isSuccessful) {
      val remoteGitHubUser = response.body()!!
      // Transform data
      val localGitHubUser = remoteGitHubUser.toLocalDataModel()
      // Update user
      localDataSource.update(localGitHubUser)
    } else {
      throw ApiCallException(response.code(), response.message())
    }
  }

  /**
   * Fetch the GitHub User by ID stored in the local database.
   */
  fun fetchLocalGitHubUser(userLogin: String): LiveData<UserWithNote> {
    return localDataSource.fetchByLogin(userLogin)
  }

  /**
   * Search keyword from in local database
   */
  suspend fun search(keyword: String): List<UserWithNote> {
    return localDataSource.search(keyword)
  }
}
