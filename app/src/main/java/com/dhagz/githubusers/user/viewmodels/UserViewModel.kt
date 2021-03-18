package com.dhagz.githubusers.user.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhagz.githubusers._remote.ApiCallException
import com.dhagz.githubusers.note.repo.NotesRepo
import com.dhagz.githubusers.user.repo.GitHubUsersRepo
import com.dhagz.githubusers.user.repo.local.UserWithNote
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserViewModel @Inject constructor(
  private val userRepo: GitHubUsersRepo,
  private val notesRepo: NotesRepo
) : ViewModel() {

  var data: UserWithNote? = null

  /**
   * TRUE if fetching user info needs to be retried.
   */
  private var isRetryFetchUserInfo = false

  /**
   * The user's login ID
   */
  private var userLogin: String? = null

  fun fetchUser(userLogin: String): LiveData<UserWithNote> {
    return userRepo.fetchLocalGitHubUser(userLogin)
  }

  fun fetchUserRemote(login: String) {
    userLogin = login
    viewModelScope.launch {
      try {
        isRetryFetchUserInfo = false
        userRepo.fetchGitHubUser(login)
      } catch (ex: ApiCallException) {
        isRetryFetchUserInfo = true
        ex.printStackTrace()
      }
    }
  }

  fun saveNote(note: String) {
    viewModelScope.launch {
      notesRepo.addUserNote(data?.user?.id!!, note)
    }
  }

  fun onNetworkStateUpdate(isInternetAvailable: Boolean) {
    if (isInternetAvailable && isRetryFetchUserInfo && userLogin != null) {
      fetchUserRemote(userLogin!!)
    }
  }
}