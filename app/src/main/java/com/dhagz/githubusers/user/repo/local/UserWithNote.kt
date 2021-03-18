package com.dhagz.githubusers.user.repo.local

import androidx.room.Embedded
import com.dhagz.githubusers.note.repo.local.Note

class UserWithNote {

  @Embedded
  lateinit var user: GitHubUser

  @Embedded
  var note: Note? = null
}