package com.dhagz.githubusers.userlist

import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dhagz.githubusers.BaseActivity
import com.dhagz.githubusers.GitHubUsersApplication
import com.dhagz.githubusers.R
import com.dhagz.githubusers.userlist.adapter.UserListAdapter
import com.dhagz.githubusers.userlist.viewmodels.UsersListViewModel
import com.dhagz.githubusers.utils.DensityUtils
import com.dhagz.githubusers.utils.KeyboardUtils
import javax.inject.Inject


class UserListActivity : BaseActivity() {

  @Inject
  lateinit var viewModel: UsersListViewModel

  // Views
  private lateinit var container: RelativeLayout
  private lateinit var keyword: EditText
  private lateinit var clear: Button
  private lateinit var divider: View
  private lateinit var recycler: RecyclerView

  private lateinit var userListAdapter: UserListAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    // Make Dagger instantiate @Inject fields
    (applicationContext as GitHubUsersApplication).appComponent.inject(this)
    super.onCreate(savedInstanceState)
    setContentView(createView())

    userListAdapter = UserListAdapter(this)
    // Fetch users
    viewModel.fetchUsers().observe(this) {
      if (!viewModel.isSearching()) {
        userListAdapter.setUsers(it)
      }
    }

    // Set recycler view
    recycler.layoutManager = LinearLayoutManager(this)
    recycler.adapter = userListAdapter
    recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
      override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (!viewModel.isSearching()
          && !recyclerView.canScrollVertically(1)
          && !userListAdapter.isLoadingMore
        ) {
          userListAdapter.showLoading()
          viewModel.loadMoreUsers(userListAdapter.getLastUserId())
          recycler.scrollToPosition(userListAdapter.itemCount)
        }
      }
    })

    // Search
    keyword.addTextChangedListener {
      val text = it.toString()
      viewModel.search(text) { data ->
        userListAdapter.setUsers(data)
      }
    }

    clear.setOnClickListener {
      keyword.setText("")
    }

    // Hide keyboard
    KeyboardUtils.hideKeyboard(this)
  }

  override fun getMainContainer(): View {
    return container
  }

  override fun showNoInternetBanner(isInternetAvailable: Boolean) {
    super.showNoInternetBanner(isInternetAvailable)
    viewModel.onNetworkStateUpdate(isInternetAvailable)
  }

  private fun createView(): View {
    container = RelativeLayout(this)
    keyword = EditText(this)
    clear = Button(this)
    divider = View(this)
    recycler = RecyclerView(this)

    // Set the ids
    container.id = R.id.user_list_container
    keyword.id = R.id.user_list_search_keyword
    clear.id = R.id.user_list_search_clear
    divider.id = R.id.user_list_search_divider
    recycler.id = R.id.user_list_recycler

    // Keyword
    val keywordLayoutParams = RelativeLayout.LayoutParams(
      DensityUtils.convertDpToPixel(this, 0.0f).toInt(),
      DensityUtils.convertDpToPixel(this, 56.0f).toInt()
    )
    keywordLayoutParams.marginStart = DensityUtils.convertDpToPixel(this, 8.0f).toInt()
    keywordLayoutParams.marginEnd = DensityUtils.convertDpToPixel(this, 8.0f).toInt()
    keywordLayoutParams.addRule(RelativeLayout.START_OF, clear.id)
    keywordLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_START)
    keyword.layoutParams = keywordLayoutParams
    keyword.inputType = InputType.TYPE_CLASS_TEXT
    keyword.hint = getString(R.string.keyword)
    container.addView(keyword)

    // Clear
    val clearLayoutParams = RelativeLayout.LayoutParams(
      RelativeLayout.LayoutParams.WRAP_CONTENT,
      DensityUtils.convertDpToPixel(this, 56.0f).toInt()
    )
    clearLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_END)
    clearLayoutParams.marginEnd = DensityUtils.convertDpToPixel(this, 8.0f).toInt()
    clear.layoutParams = clearLayoutParams
    clear.setText(R.string.clear)
    container.addView(clear)

    // Divider
    val dividerLayoutParams = RelativeLayout.LayoutParams(
      RelativeLayout.LayoutParams.MATCH_PARENT,
      DensityUtils.convertDpToPixel(this, 1.0f).toInt()
    )
    dividerLayoutParams.addRule(RelativeLayout.BELOW, keyword.id)
    dividerLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_START)
    dividerLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_END)
    divider.layoutParams = dividerLayoutParams
    divider.setBackgroundColor(ContextCompat.getColor(this, R.color.divider))
    container.addView(divider)

    // Recycler
    val recyclerLayoutParams = RelativeLayout.LayoutParams(
      RelativeLayout.LayoutParams.MATCH_PARENT,
      DensityUtils.convertDpToPixel(this, 0.0f).toInt()
    )
    recyclerLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
    recyclerLayoutParams.addRule(RelativeLayout.BELOW, divider.id)
    recycler.layoutParams = recyclerLayoutParams
    container.addView(recycler)

    return container
  }
}