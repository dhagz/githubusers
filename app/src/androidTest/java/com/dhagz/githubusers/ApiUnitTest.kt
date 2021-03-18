package com.dhagz.githubusers

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dhagz.githubusers._room.AppDatabase
import com.dhagz.githubusers.note.repo.NotesRepo
import com.dhagz.githubusers.note.repo.local.NoteDao
import com.dhagz.githubusers.user.repo.local.GitHubUserDao
import com.dhagz.githubusers.user.repo.remote.GitHubUsersService
import com.dhagz.githubusers.utils.LifeCycleTestOwner
import com.dhagz.githubusers.utils.NetworkUtils
import kotlinx.coroutines.runBlocking
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

/**
 * Room database unit test
 */
@RunWith(AndroidJUnit4::class)
class ApiUnitTest {

  private lateinit var context: Context

  private lateinit var db: AppDatabase
  private lateinit var usersDao: GitHubUserDao
  private lateinit var notesDao: NoteDao
  private lateinit var notesRepo: NotesRepo
  private lateinit var apiUrl: String
  private lateinit var retrofit: Retrofit
  private lateinit var gitHubUsersApi: GitHubUsersService

  private lateinit var lifeCycleTestOwner: LifeCycleTestOwner

  private val handler = Handler(Looper.getMainLooper());

  @Before
  fun setUp() {
    context = ApplicationProvider.getApplicationContext<Context>()

    // Database
    db = Room.inMemoryDatabaseBuilder(
      context, AppDatabase::class.java
    ).build()
    usersDao = db.gitHubUserDao()
    notesDao = db.noteDao()
    notesRepo = NotesRepo(notesDao)

    // LiveData
    lifeCycleTestOwner = LifeCycleTestOwner()
    lifeCycleTestOwner.onCreate()

    // API URL
    apiUrl = context.getString(R.string.api_base_url)

    // Initialize Retrofit
    val dispatcher = Dispatcher()
    dispatcher.maxRequests = 1
    val okHttpClient = OkHttpClient.Builder()
      .dispatcher(dispatcher)
      .build()
    retrofit = Retrofit.Builder()
      .baseUrl(apiUrl)
      .addConverterFactory(GsonConverterFactory.create())
      .client(okHttpClient)
      .build()
    gitHubUsersApi = retrofit.create(GitHubUsersService::class.java)
  }

  @After
  @Throws(IOException::class)
  fun tearDown() {
    // Database
    db.close()

    // Cannot invoke removeObserver on a background thread
    handler.post {
      lifeCycleTestOwner.onDestroy()
    }
  }

  @Test
  @Throws(Exception::class)
  fun fetchUserTest() {
    runBlocking {
      if (NetworkUtils.isApiAvailable(apiUrl)) {
        val res = gitHubUsersApi.fetchUser("tawk")
        if (res.isSuccessful) {
          assertThat(res.body()?.login, equalTo("tawk"))
        }
      }
    }
  }

  @Test
  @Throws(Exception::class)
  fun fetchUserListTest() {
    runBlocking {
      if (NetworkUtils.isApiAvailable(apiUrl)) {
        val res = gitHubUsersApi.fetchUsers(0)
        if (res.isSuccessful) {
          assertThat(res.body()?.get(0)?.id, equalTo(1))
          assertThat(res.body()?.get(0)?.login, equalTo("mojombo"))
        }
      }
    }
  }
}