package com.dhagz.githubusers.note.repo.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey
import com.dhagz.githubusers.user.repo.local.GitHubUser

@Entity(
  tableName = "notes",
  foreignKeys = [ForeignKey(
    entity = GitHubUser::class,
    parentColumns = ["id"],
    childColumns = ["userId"],
    onDelete = CASCADE
  )],
  indices = [Index(value = ["userId"])]
)
data class Note(
  @PrimaryKey
  val userId: Long,
  val note: String?
)