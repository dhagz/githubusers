package com.dhagz.githubusers._di

import android.content.Context
import androidx.room.Room
import com.dhagz.githubusers._di.qualifiers.AppContextQualifier
import com.dhagz.githubusers._room.AppDatabase
import com.dhagz.githubusers.note.repo.local.NoteDao
import com.dhagz.githubusers.user.repo.local.GitHubUserDao
import dagger.Module
import dagger.Provides

@Module
class DatabaseModule {

  @Provides
  fun appDatabase(@AppContextQualifier context: Context): AppDatabase {
    return Room.databaseBuilder(context, AppDatabase::class.java, "github-db")
      .fallbackToDestructiveMigration()
      .build()
  }

  @Provides
  fun userDao(appDatabase: AppDatabase): GitHubUserDao {
    return appDatabase.gitHubUserDao()
  }

  @Provides
  fun noteDao(appDatabase: AppDatabase): NoteDao {
    return appDatabase.noteDao()
  }
}