package com.prmto.artbookhilttesting.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import com.prmto.artbookhilttesting.R
import com.prmto.artbookhilttesting.adapter.ImageRecyclerAdapter
import com.prmto.artbookhilttesting.getOrAwaitValue
import com.prmto.artbookhilttesting.launchFragmentInHiltContainer
import com.prmto.artbookhilttesting.repo.FakeArtRepository
import com.prmto.artbookhilttesting.viewmodel.ArtViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import javax.inject.Inject

@HiltAndroidTest
@MediumTest
@ExperimentalCoroutinesApi
class ImageApıFragmentTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var fragmentFactory: ArtFragmentFactory

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun selectImage() {
        val navController = Mockito.mock(NavController::class.java)

        val selectedImageUrl = "www.image.com"

        val testViewModel = ArtViewModel(FakeArtRepository())

        launchFragmentInHiltContainer<ImageApiFragment>(
            fragmentFactory = fragmentFactory
        ) {
            Navigation.setViewNavController(requireView(), navController)
            val imageApi = this as ImageApiFragment
            imageApi.viewModel = testViewModel
            imageApi.imageRecyclerAdapter.images = listOf(selectedImageUrl)
        }

        onView(withId(R.id.imageRecyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<ImageRecyclerAdapter.ImageViewHolder>(
                0, click()
            )
        )

        Mockito.verify(navController).popBackStack()

        assertThat(testViewModel.selectedImage.getOrAwaitValue()).isEqualTo(selectedImageUrl)
    }

}