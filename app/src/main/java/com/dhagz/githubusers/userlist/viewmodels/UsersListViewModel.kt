package com.dhagz.githubusers.userlist.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhagz.githubusers._remote.ApiCallException
import com.dhagz.githubusers.user.repo.GitHubUsersRepo
import com.dhagz.githubusers.user.repo.local.UserWithNote
import kotlinx.coroutines.launch
import javax.inject.Inject

class UsersListViewModel @Inject constructor(private val userRepo: GitHubUsersRepo) : ViewModel() {

  /**
   * TRUE if the user is searching
   */
  private var isSearching = false

  /**
   * TRUE if fetching user info needs to be retried.
   */
  private var isRetryLoadMoreUsers = false

  /**
   * The user's ID
   */
  private var lastUserId: Long? = null

  fun fetchUsers(): LiveData<List<UserWithNote>> {
    return userRepo.fetchLocalGitHubUsers()
  }

  fun loadMoreUsers(lastUserId: Long) {
    // Don't load more if the user is searching
    if (isSearching) return

    this.lastUserId = lastUserId
    viewModelScope.launch {
      try {
        isRetryLoadMoreUsers = false
        userRepo.fetchGitHubUsers(lastUserId)
      } catch (ex: ApiCallException) {
        isRetryLoadMoreUsers = true
        ex.printStackTrace()
      }
    }
  }

  fun search(keyword: String, callback: (result: List<UserWithNote>) -> Unit) {
    isSearching = true

    viewModelScope.launch {
      callback(userRepo.search(keyword))
    }

    // Let it search an empty keyword to display all
    // Then, stop search
    if (keyword.isEmpty()) {
      stopSearch()
      return
    }
  }

  fun isSearching(): Boolean {
    return isSearching
  }

  fun stopSearch() {
    isSearching = false
  }

  fun onNetworkStateUpdate(isInternetAvailable: Boolean) {
    if (isInternetAvailable && isRetryLoadMoreUsers && lastUserId != null) {
      loadMoreUsers(lastUserId!!)
    }
  }
}