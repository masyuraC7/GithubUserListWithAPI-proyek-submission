package com.mc7.aplikasigithubuser.ui

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.mc7.aplikasigithubuser.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.TypeSafeMatcher
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {

    @Before
    fun setup(){
        ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun testTopAppBarAction() {
        runBlocking {
            onView(withId(R.id.settings)).perform(click())
            delay(1000)
            pressBack()
            delay(1000)
            onView(withId(R.id.favorite)).perform(click())
            delay(1000)
        }
    }

    @Test
    fun testItemClick() {
        runBlocking {
            delay(3000)
            onView(withId(R.id.rv_list_user_github)).perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    1, click()
                )
            )
            delay(2000)
        }
    }

    @Test
    fun testMaterialSearchView() {
        runBlocking {
            delay(3000)
            val searchBar = onView(
                Matchers.allOf(
                    withId(R.id.sb_user_github),
                    childAtPosition(
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        ),
                        1
                    ),
                    isDisplayed()
                )
            )
            searchBar.perform(click())

            delay(1000)

            val appCompatEditText = onView(
                Matchers.allOf(
                    withId(com.google.android.material.R.id.search_view_edit_text),
                    childAtPosition(
                        childAtPosition(
                            withId(com.google.android.material.R.id.search_view_toolbar),
                            1
                        ),
                        1
                    ),
                    isDisplayed()
                )
            )
            appCompatEditText.perform(click())

            delay(1000)

            val appCompatEditText2 = onView(
                Matchers.allOf(
                    withId(com.google.android.material.R.id.search_view_edit_text),
                    childAtPosition(
                        childAtPosition(
                            withId(com.google.android.material.R.id.search_view_toolbar),
                            1
                        ),
                        1
                    ),
                    isDisplayed()
                )
            )
            appCompatEditText2.perform(replaceText("ariporad"), closeSoftKeyboard())

            delay(2000)

            val appCompatEditText3 = onView(
                Matchers.allOf(
                    withId(com.google.android.material.R.id.search_view_edit_text),
                    withText("ariporad"),
                    childAtPosition(
                        childAtPosition(
                            withId(com.google.android.material.R.id.search_view_toolbar),
                            1
                        ),
                        1
                    ),
                    isDisplayed()
                )
            )
            appCompatEditText3.perform(pressImeActionButton())

            val frameLayout = onView(
                Matchers.allOf(
                    withId(R.id.sv_user_github),
                    withParent(withParent(withId(android.R.id.content))),
                    isDisplayed()
                )
            )
            frameLayout.check(ViewAssertions.matches(isDisplayed()))

            delay(2000)
        }
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}