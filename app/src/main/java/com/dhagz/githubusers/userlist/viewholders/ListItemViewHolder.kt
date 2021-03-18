package com.dhagz.githubusers.userlist.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class ListItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

  abstract fun onBind(holder: ListItemViewHolder, position: Int)

  open fun onRecycle(holder: ListItemViewHolder?) {
    //
  }

}