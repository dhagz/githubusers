package com.dhagz.githubusers.note.repo.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NoteDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertUserNote(note: Note)
}