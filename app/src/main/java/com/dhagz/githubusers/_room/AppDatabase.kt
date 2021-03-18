package com.dhagz.githubusers._room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dhagz.githubusers.note.repo.local.Note
import com.dhagz.githubusers.note.repo.local.NoteDao
import com.dhagz.githubusers.user.repo.local.GitHubUser
import com.dhagz.githubusers.user.repo.local.GitHubUserDao

@Database(entities = [GitHubUser::class, Note::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

  abstract fun gitHubUserDao(): GitHubUserDao

  abstract fun noteDao(): NoteDao
}