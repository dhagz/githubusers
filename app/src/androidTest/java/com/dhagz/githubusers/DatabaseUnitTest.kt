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
import com.dhagz.githubusers.user.repo.GitHubUsersRepo
import com.dhagz.githubusers.user.repo.local.GitHubUserDao
import com.dhagz.githubusers.user.viewmodels.UserViewModel
import com.dhagz.githubusers.utils.LifeCycleTestOwner
import com.dhagz.githubusers.utils.TestUtils
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * Room database unit test
 */
@RunWith(AndroidJUnit4::class)
class DatabaseUnitTest {

  private lateinit var context: Context

  private lateinit var db: AppDatabase
  private lateinit var usersDao: GitHubUserDao
  private lateinit var notesDao: NoteDao
  private lateinit var notesRepo: NotesRepo

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
  }

  @After
  @Throws(IOException::class)
  fun tearDown() {
    // Database
    db.close()

    // LiveData
    // Cannot invoke removeObserver on a background thread
    handler.post {
      lifeCycleTestOwner.onDestroy()
    }
  }

  @Test
  @Throws(Exception::class)
  fun insertFetchTest() {

    val users = TestUtils.justTony()

    // Test with Coroutines
    runBlocking {
      val res = usersDao.insertAll(users)
      // Check if there was some data inserted
      assertThat(res.size, equalTo(1))
      // Check if the user ID is correct
      assertThat(res[0], equalTo(3000))
    }

    // Test with LiveData
    handler.post {
      usersDao.fetchByLogin("iamironman").observe(lifeCycleTestOwner, {
        assertThat(it.user.login, equalTo("iamironman"))
        assertThat(it.user.name, equalTo("Tony Stark"))
      })
    }
  }

  @Test
  @Throws(Exception::class)
  fun searchTest() {

    val users = TestUtils.testUsers()

    // Test with Coroutines
    runBlocking {
      val res = usersDao.insertAll(users)
      // Check if there was some data inserted
      assertThat(res.size, equalTo(2))

      val searchRes = usersDao.search("iron")
      assertThat(searchRes.size, equalTo(2))

      val searchRes2 = usersDao.search("ironm")
      assertThat(searchRes2.size, equalTo(1))
    }
  }

  @Test
  @Throws(Exception::class)
  fun updateTest() {

    val users = TestUtils.justTony()

    // Test with Coroutines
    runBlocking {
      val res = usersDao.insertAll(users)

      // Check if there was some data inserted
      assertThat(res.size, equalTo(1))

      val tonya = TestUtils.tanya.copy(id = TestUtils.tony.id, login = "iamironman")
      usersDao.update(tonya)
    }

    // Test with LiveData
    handler.post {
      usersDao.fetchByLogin("iamironman").observe(lifeCycleTestOwner, {
        assertThat(it.user.login, equalTo("iamironman"))
        assertThat(it.user.name, equalTo("Tanya Stonks"))
        assertThat(it.user.company, equalTo("Stonks Market"))
      })
    }
  }

  @Test
  @Throws(Exception::class)
  fun addNoteTest() {
    val users = TestUtils.justTony()

    // Test with Coroutines
    runBlocking {
      val res = usersDao.insertAll(users)
      // Check if there was some data inserted
      assertThat(res.size, equalTo(1))

      // Add the note
      notesRepo.addUserNote(users[0].id, "(snap)")
    }

    handler.post {
      // Test if the note was added
      usersDao.fetchByLogin("iamironman").observe(lifeCycleTestOwner, {
        assertThat(it.note?.note, equalTo("(snap)"))
      })
    }

    // Update the note
    runBlocking {
      notesRepo.addUserNote(users[0].id, "(snap) (dead)")
    }

    handler.post {
      // Test if the note was added
      usersDao.fetchByLogin("iamironman").observe(lifeCycleTestOwner, {
        assertThat(it.note?.note, equalTo("(snap) (dead)"))
      })
    }
  }
}