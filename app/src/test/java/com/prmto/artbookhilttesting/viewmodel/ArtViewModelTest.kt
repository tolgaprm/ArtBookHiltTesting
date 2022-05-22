package com.prmto.artbookhilttesting.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.prmto.artbookhilttesting.MainCoroutineRule
import com.prmto.artbookhilttesting.getOrAwaitValueTest
import com.prmto.artbookhilttesting.repo.FakeArtRepository
import com.prmto.artbookhilttesting.roomdb.Art
import com.prmto.artbookhilttesting.util.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ArtViewModelTest {

    // Bu testlerdeki kodları main therad'da çalıştırmamızı sağlıyor
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    // Main theradde çalışması için çünkü şuanda unit test yapıyoruz yani
    // android device üazerinde çalışmıyoruz ve main therad yok
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()


    private lateinit var viewModel: ArtViewModel

    @Before
    fun setup() {
        // Test Doubles
        viewModel = ArtViewModel(FakeArtRepository())
    }

    @Test
    fun `insert art without year returns error`() {
        viewModel.makeArt("Mona Lisa", "Da Vinci", "")
        val value = viewModel.insertArtMsg.getOrAwaitValueTest()
        // Bu değer bir liveData, testlerde async çalışan bir şey istemiyoruz.
        // getOrAwaitValueTest -> işlemi liveData'yı normal dataya çeviriyor

        assertThat(value.message).isEqualTo("Enter name,artist,year")

    }

    @Test
    fun `insert art without name returns error`() {
        viewModel.makeArt("", "Da Vinci", "1200")
        val value = viewModel.insertArtMsg.getOrAwaitValueTest()
        // Bu değer bir liveData, testlerde async çalışan bir şey istemiyoruz.
        // getOrAwaitValueTest -> işlemi liveData'yı normal dataya çeviriyor

        assertThat(value.message).isEqualTo("Enter name,artist,year")
    }

    @Test
    fun `insert art without artist name returns error`() {
        viewModel.makeArt("Mona Lisa", "", "1200")
        val value = viewModel.insertArtMsg.getOrAwaitValueTest()
        // Bu değer bir liveData, testlerde async çalışan bir şey istemiyoruz.
        // getOrAwaitValueTest -> işlemi liveData'yı normal dataya çeviriyor

        assertThat(value.message).isEqualTo("Enter name,artist,year")
    }

}