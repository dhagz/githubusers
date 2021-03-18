package com.dhagz.githubusers.note.repo

import com.dhagz.githubusers.note.repo.local.Note
import com.dhagz.githubusers.note.repo.local.NoteDao
import javax.inject.Inject


class NotesRepo @Inject constructor(
  private val localDataSource: NoteDao
) {

  /**
   * Update user note.
   *
   * @param userId the user id
   * @param note the note
   */
  suspend fun addUserNote(userId: Long, note: String?) {
    localDataSource.insertUserNote(Note(userId, note))
  }
}
