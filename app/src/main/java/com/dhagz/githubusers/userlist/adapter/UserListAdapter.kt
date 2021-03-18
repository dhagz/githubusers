package com.dhagz.githubusers.userlist.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dhagz.githubusers.R
import com.dhagz.githubusers.user.repo.local.UserWithNote
import com.dhagz.githubusers.userlist.viewholders.ListItemViewHolder
import com.dhagz.githubusers.userlist.viewholders.LoadingItemViewHolder
import com.dhagz.githubusers.userlist.viewholders.UserItemViewHolder
import java.util.*

class UserListAdapter(val context: Context) : RecyclerView.Adapter<ListItemViewHolder>() {

  private val inflater: LayoutInflater = LayoutInflater.from(context)
  private val users = LinkedList<UserWithNote>()

  var isLoadingMore = false

  override fun getItemViewType(position: Int): Int {
    return if (position < users.size) {
      TYPE_USER
    } else {
      TYPE_LOADING
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
    val view: View
    return when (viewType) {
      TYPE_USER -> {
        view = inflater.inflate(R.layout.listitem_user, parent, false)
        UserItemViewHolder(view, context, users)
      }
      else -> {
        view = inflater.inflate(R.layout.listitem_loading, parent, false)
        LoadingItemViewHolder(view)
      }
    }
  }

  override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
    holder.onBind(holder, position)
  }

  override fun getItemCount(): Int {
    var result = users.size
    if (isLoadingMore) {
      result++
    }
    return result
  }

  override fun onViewRecycled(holder: ListItemViewHolder) {
    super.onViewRecycled(holder)
    holder.onRecycle(holder)
  }

  fun setUsers(newUsers: List<UserWithNote>) {
    isLoadingMore = false
    users.clear()
    users.addAll(newUsers)
    notifyDataSetChanged()
  }

  fun getLastUserId(): Long {
    return if (users.size != 0) {
      users.last.user.id
    } else {
      0
    }
  }

  fun showLoading() {
    isLoadingMore = true
    notifyDataSetChanged()
  }

  companion object {
    private const val TYPE_USER = 1
    private const val TYPE_LOADING = 2
  }
}