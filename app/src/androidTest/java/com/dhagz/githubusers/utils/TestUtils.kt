package com.dhagz.githubusers.utils

import com.dhagz.githubusers.user.repo.local.GitHubUser
import java.util.*

class TestUtils {
  companion object {
    val tony = GitHubUser(
      id = 3000,
      login = "iamironman",
      nodeId = "nodeId",
      avatarUrl = "https://www.iron.man/i.jpg",
      url = "https://www.iron.man",
      htmlUrl = "https://www.iron.man",
      type = "Iron",
      name = "Tony Stark",
      company = "Stark Industries",
      blog = "https://www.iron.man",
      location = "10880 Malibu Point",
      email = "i.am@iron.man",
      bio = "Genius, Billionaire, Playboy, Philanthropist",
      publicRepos = 1,
      publicGists = 2,
      followers = 3,
      following = 4,
      createdAt = "_",
      updatedAt = "_"
    )

    val tanya = GitHubUser(
      id = 2999,
      login = "iamironwoman",
      nodeId = "nodeId",
      avatarUrl = "https://www.iron.man/asdf.tiff",
      url = "https://www.iron.woman",
      htmlUrl = "https://www.iron.woman",
      type = "Nickel",
      name = "Tanya Stonks",
      company = "Stonks Market",
      blog = "https://www.iron.woman",
      location = "10880 Malibu Nights",
      email = "me.is@iron.woman",
      bio = "Stonks!",
      publicRepos = 1,
      publicGists = 2,
      followers = 3,
      following = 4,
      createdAt = "_",
      updatedAt = "_"
    )

    fun testUsers(): List<GitHubUser> {
      val res = LinkedList<GitHubUser>()

      res.add(tony)
      res.add(tanya)

      return res
    }

    fun justTony(): List<GitHubUser> {
      val res = LinkedList<GitHubUser>()
      res.add(tony)
      return res
    }
  }
}