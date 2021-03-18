package com.dhagz.githubusers.userlist.viewholders

import android.content.Context
import android.content.Intent
import android.graphics.ColorMatrixColorFilter
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dhagz.githubusers.R
import com.dhagz.githubusers.user.UserActivity
import com.dhagz.githubusers.user.repo.local.UserWithNote

class UserItemViewHolder(
  view: View,
  private val context: Context,
  private val users: List<UserWithNote>
) :
  ListItemViewHolder(view), View.OnClickListener {

  private val avatar: ImageView = view.findViewById(R.id.avatar)
  private val username: TextView = view.findViewById(R.id.username)
  private val details: TextView = view.findViewById(R.id.details)
  private val noteIcon: ImageView = view.findViewById(R.id.note_icon)

  private var data: UserWithNote? = null

  private val NEGATIVE = floatArrayOf(
    -1.0f, 0f, 0f, 0f, 255f, // red
    0f, -1.0f, 0f, 0f, 255f, // green
    0f, 0f, -1.0f, 0f, 255f, // blue
    0f, 0f, 0f, 1.0f, 0f     // alpha
  )

  init {
    view.setOnClickListener(this)
  }

  override fun onBind(holder: ListItemViewHolder, position: Int) {
    data = users[position]

    username.text = data?.user?.login
    details.text = data?.user?.htmlUrl

    if (data?.note != null && data?.note?.note?.isNotEmpty()!!) {
      noteIcon.visibility = View.VISIBLE
    } else {
      noteIcon.visibility = View.GONE
    }

    Glide.with(itemView)
      .load(data?.user?.avatarUrl)
      .placeholder(R.drawable.ic_user_56)
      .diskCacheStrategy(DiskCacheStrategy.ALL)
      .circleCrop()
      .into(avatar)

    if ((position + 1) % 4 == 0) {
      avatar.colorFilter = ColorMatrixColorFilter(NEGATIVE)
    } else {
      avatar.clearColorFilter()
    }
  }

  override fun onRecycle(holder: ListItemViewHolder?) {
    super.onRecycle(holder)
    data = null
  }

  override fun onClick(view: View?) {
    val intent = Intent(context, UserActivity::class.java)
    intent.putExtra(UserActivity.EXTRA_USER_LOGIN, data?.user?.login)
    context.startActivity(intent)
  }
}