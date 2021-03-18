package com.dhagz.githubusers.user

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dhagz.githubusers.BaseActivity
import com.dhagz.githubusers.GitHubUsersApplication
import com.dhagz.githubusers.R
import com.dhagz.githubusers.databinding.ActivityUserBinding
import com.dhagz.githubusers.user.repo.local.UserWithNote
import com.dhagz.githubusers.user.viewmodels.UserViewModel
import com.dhagz.githubusers.utils.KeyboardUtils
import kotlinx.android.synthetic.main.activity_user.*
import javax.inject.Inject


class UserActivity : BaseActivity() {

  @Inject
  lateinit var viewModel: UserViewModel

  private lateinit var userLogin: String
  private lateinit var binding: ActivityUserBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    (applicationContext as GitHubUsersApplication).appComponent.inject(this)
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this, R.layout.activity_user)
    binding.viewModel = viewModel
    binding.lifecycleOwner = this

    supportActionBar?.setDisplayHomeAsUpEnabled(true);

    with(intent) {
      val login = getStringExtra(EXTRA_USER_LOGIN)
      if (login == null) {
        // Show error and exit activity
        Toast.makeText(this@UserActivity, R.string.err_user_not_found, Toast.LENGTH_SHORT).show()
        finish()
        return@onCreate
      } else {
        userLogin = login
      }
    }

    // Fetch the user
    viewModel.fetchUser(userLogin).observe(this, { data ->
      populateData(data)
    })

    // Fetch user from remote
    viewModel.fetchUserRemote(userLogin)

    save.setOnClickListener {
      viewModel.saveNote(note.text.toString())
      KeyboardUtils.hideKeyboard(this)
      Toast.makeText(this@UserActivity, R.string.info_note_saved, Toast.LENGTH_SHORT).show()
    }

    // Hide keyboard
    KeyboardUtils.hideKeyboard(this)
  }

  override fun getMainContainer(): View {
    return container_main
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      android.R.id.home -> {
        onBackPressed()
        return true
      }
    }
    return super.onOptionsItemSelected(item)
  }

  override fun showNoInternetBanner(isInternetAvailable: Boolean) {
    super.showNoInternetBanner(isInternetAvailable)
    viewModel.onNetworkStateUpdate(isInternetAvailable)
  }

  private fun populateData(data: UserWithNote) {
    viewModel.data = data
    binding.data = data
    supportActionBar?.title = data.user.login

    Glide.with(this)
      .load(data.user.avatarUrl)
      .placeholder(R.drawable.ic_user_56)
      .diskCacheStrategy(DiskCacheStrategy.ALL)
      .circleCrop()
      .into(avatar)
  }

  companion object {
    const val EXTRA_USER_LOGIN: String = "EXTRA_USER_LOGIN"
  }
}