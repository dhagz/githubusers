package com.dhagz.githubusers.user.repo.remote

import com.dhagz.githubusers.user.repo.local.GitHubUser
import com.google.gson.annotations.SerializedName

class RemoteGitHubUser(
  val id: Long,
  val login: String?,
  @SerializedName("node_id")
  val nodeId: String?,
  @SerializedName("avatar_url")
  val avatarUrl: String?,
  val url: String?,
  @SerializedName("html_url")
  val htmlUrl: String?,
  val type: String?,
  val name: String?,
  val company: String?,
  val blog: String?,
  val location: String?,
  val email: String?,
  val bio: String?,
  @SerializedName("public_repos")
  val publicRepos: Long?,
  @SerializedName("public_gists")
  val publicGists: Long?,
  val followers: Long?,
  val following: Long?,
  @SerializedName("created_at")
  val createdAt: String?,
  @SerializedName("updated_at")
  val updatedAt: String?
) {
  fun toLocalDataModel(): GitHubUser {
    return GitHubUser(
      id = id,
      login = login,
      nodeId = nodeId,
      avatarUrl = avatarUrl,
      url = url,
      htmlUrl = htmlUrl,
      type = type,
      name = name,
      company = company,
      blog = blog,
      location = location,
      email = email,
      bio = bio,
      publicRepos = publicRepos,
      publicGists = publicGists,
      followers = followers,
      following = following,
      createdAt = createdAt,
      updatedAt = updatedAt
    )
  }
}