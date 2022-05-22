package com.prmto.artbookhilttesting.roomdb

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.prmto.artbookhilttesting.getOrAwaitValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@SmallTest // Bunu koymasak da olabilir
@ExperimentalCoroutinesApi
@HiltAndroidTest // Bunu hilti kullanarak test ettiğimizi söylüyoruz
class ArtDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @Named("testDatabase")
    lateinit var database: ArtDatabase

    private lateinit var dao: ArtDao


    @Before
    fun setup() {
        // inMemoryDatabase diyerek oluşturursak bunu ramde oluşturuyor ve işimiz bittiğinde siliniyor.
        /* database = Room.inMemoryDatabaseBuilder(
             ApplicationProvider.getApplicationContext(),
             ArtDatabase::class.java
         ).allowMainThreadQueries().build()
 */
        hiltRule.inject()
        dao = database.artDao
    }

    @After
    fun teardown() {
        database.close()
    }

    // Bu işlemler suspend func olduğu için coroıutine kullanmamız gerkiyor
    // Normalde burada runBlockingTest kullanmamız gerkeiyor ama o deprecated edlimiş onun yerine bunu kullanıyoruz.
    @Test
    fun insertArtTesting() = runTest {
        val exampleArt = Art(1, "Mona Lisa", "Da Vinci", 1784, "test.com")
        dao.insertArt(exampleArt)

        val list = dao.observeArts().getOrAwaitValue()
        assertThat(list).contains(exampleArt)
    }

    @Test
    fun deleteArtTesting() = runTest {
        val exampleArt = Art(1, "Mona Lisa", "Da Vinci", 1784, "test.com")
        dao.insertArt(exampleArt)
        dao.delete(exampleArt)
        val list = dao.observeArts().getOrAwaitValue()
        assertThat(list).doesNotContain(exampleArt)
    }
}