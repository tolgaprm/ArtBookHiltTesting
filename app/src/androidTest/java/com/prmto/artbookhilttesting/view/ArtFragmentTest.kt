package com.prmto.artbookhilttesting.view

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.MediumTest
import com.prmto.artbookhilttesting.R
import com.prmto.artbookhilttesting.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import javax.inject.Inject

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class ArtFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var fragmentFactory: ArtFragmentFactory

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun testNavigationFromArtsToArtDetails() {

        /*
        * Fake -> Tamamen sahte bir repo önceden yazdığımız gibi, burda tamamen test amaçlı, içerisine uydurma listeler vs. koyduğumuz,
        yapılar
        *
        * Mocks -> Biraz daha komplex , yani tamamen oradki sınıfın kendisini kopyaladığımız fakat kendi ihtiyaçlarımız doğrultusunda
        * kullandığımız bazı expections(beklentiler) söylediğimiz yapılar
        * */

        val navController = Mockito.mock(NavController::class.java)

        // Bu hilt'le birlikte fragmnetları kullanmamıza olanak tanıyor. Fragmentları açmamız/oluşturmamız için kullanılır.
        launchFragmentInHiltContainer<ArtFragment>(
            fragmentFactory = fragmentFactory
        ) {
            Navigation.setViewNavController(requireView(),navController)
        }

       Espresso.onView(ViewMatchers.withId(R.id.fab)).perform(ViewActions.click())
        Mockito.verify(navController).navigate(
            ArtFragmentDirections.actionArtFragmentToArtDetailsFragment()
        )
    }
}