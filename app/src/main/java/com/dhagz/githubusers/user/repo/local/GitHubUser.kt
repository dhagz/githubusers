package com.dhagz.githubusers.user.repo.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 'users' database table.
 */
@Entity(tableName = "users")
data class GitHubUser(
  @PrimaryKey
  val id: Long,
  val login: String?,
  @ColumnInfo(name = "node_id")
  val nodeId: String?,
  @ColumnInfo(name = "avatar_url")
  val avatarUrl: String?,
  val url: String?,
  @ColumnInfo(name = "html_url")
  val htmlUrl: String?,
  val type: String?,
  val name: String?,
  val company: String?,
  val blog: String?,
  val location: String?,
  val email: String?,
  val bio: String?,
  @ColumnInfo(name = "public_repos")
  val publicRepos: Long?,
  @ColumnInfo(name = "public_gists")
  val publicGists: Long?,
  val followers: Long?,
  val following: Long?,
  @ColumnInfo(name = "created_at")
  val createdAt: String?,
  @ColumnInfo(name = "updated_at")
  val updatedAt: String?
)