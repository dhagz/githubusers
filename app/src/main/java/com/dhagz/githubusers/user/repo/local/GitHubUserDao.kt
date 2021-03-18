package com.dhagz.githubusers.user.repo.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface GitHubUserDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(users: List<GitHubUser>): List<Long>

  @Update
  suspend fun update(user: GitHubUser)

  @Query("SELECT * FROM users LEFT JOIN notes ON users.id = notes.userId")
  fun fetchAll(): LiveData<List<UserWithNote>>

  @Query("SELECT * FROM users LEFT JOIN notes ON users.id = notes.userId WHERE users.login = :userLogin")
  fun fetchByLogin(userLogin: String): LiveData<UserWithNote>

  @Query("SELECT * FROM users LEFT JOIN notes ON users.id = notes.userId WHERE users.login LIKE '%' || :keyword || '%' OR notes.note LIKE '%' || :keyword || '%'")
  suspend fun search(keyword: String): List<UserWithNote>
}