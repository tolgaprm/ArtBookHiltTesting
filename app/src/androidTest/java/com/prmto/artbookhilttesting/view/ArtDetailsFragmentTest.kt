package com.prmto.artbookhilttesting.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import com.prmto.artbookhilttesting.R
import com.prmto.artbookhilttesting.getOrAwaitValue
import com.prmto.artbookhilttesting.launchFragmentInHiltContainer
import com.prmto.artbookhilttesting.repo.FakeArtRepository
import com.prmto.artbookhilttesting.roomdb.Art
import com.prmto.artbookhilttesting.viewmodel.ArtViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import javax.inject.Inject

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class ArtDetailsFragmentTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var fragmentFactory: ArtFragmentFactory

    lateinit var navController: NavController

    @Before
    fun setup() {
        hiltRule.inject()
        navController = Mockito.mock(NavController::class.java)
    }

    @Test
    fun navigateFromDetailFragmentToImageAPIFragmentWhenImageViewClicked() {


        launchFragmentInHiltContainer<ArtDetailsFragment>(fragmentFactory = fragmentFactory) {
            Navigation.setViewNavController(requireView(), navController)
        }

        Espresso.onView(ViewMatchers.withId(R.id.artImageView)).perform(click())

        Mockito.verify(navController).navigate(
            ArtDetailsFragmentDirections.actionArtDetailsFragmentToImageApiFragment()
        )

    }

    @Test
    fun navigateFromDetailFragmentToArtFragmentWhenOnBackPressClicked() {

        launchFragmentInHiltContainer<ArtDetailsFragment>(fragmentFactory = fragmentFactory) {
            Navigation.setViewNavController(requireView(), navController)
        }


        Espresso.pressBack()

        Mockito.verify(navController).popBackStack()

    }

    @Test
    fun testSave() {
        val testViewModel = ArtViewModel(FakeArtRepository())

        launchFragmentInHiltContainer<ArtDetailsFragment>(
            fragmentFactory = fragmentFactory
        ) {
            val fragment = this as ArtDetailsFragment
            fragment.viewModel = testViewModel
        }
        Espresso.onView(ViewMatchers.withId(R.id.nameText))
            .perform(ViewActions.replaceText("Mona Lisa"))

        Espresso.onView(ViewMatchers.withId(R.id.artistName))
            .perform(ViewActions.replaceText("Da Vinci"))

        Espresso.onView(ViewMatchers.withId(R.id.yearText))
            .perform(ViewActions.replaceText("1973"))

        Espresso.onView(withId(R.id.btnSave)).perform(click())

        assertThat(testViewModel.artList.getOrAwaitValue()).contains(
            Art(name = "Mona Lisa", artistName = "Da Vinci", year = 1973, imageUrl = "")
        )

    }

}