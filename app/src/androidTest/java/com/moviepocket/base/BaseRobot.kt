package com.diego.tweetssentimentsanalyzer.base

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import com.moviepocket.R
import com.moviepocket.base.util.RecyclerViewMatcher
import org.hamcrest.Matchers
import org.hamcrest.Matchers.allOf
import java.lang.Exception

open class BaseRobot {
    fun checkText(id: Int, text: String) {
        onView(ViewMatchers.withId(id)).check(ViewAssertions.matches(ViewMatchers.withText(text)))
    }

    fun waitForAWhile(time: Long): BaseRobot {
        Thread.sleep(time)
        return this
    }

    fun isItemVisible(itemId: Int) {
        onView(ViewMatchers.withId(itemId)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    fun isItemInVisible(itemId: Int) {
        onView(ViewMatchers.withId(itemId)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())))
    }

    fun clickRecyclerViewItem(listId: Int, itemPosition: Int, itemId: Int): BaseRobot {
        Log.d("TEST", "BEFORE CLICK ON ITEM")
        try {
            onView(allOf(withId(listId), isDisplayed()))
                    .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(itemPosition, click()))
            Log.d("TEST", "AFTER CLICK ON ITEM")
        } catch (e: Exception) {
            Log.d("TEST", "AFTER CLICK EXCEPTION")
        }
        return this
    }

    fun assertItTakeMeToScreen(targetClass: Class<*>): BaseRobot {
        intended(hasComponent(targetClass.name))
        Log.d("TEST", "ASSERT NEW SCREEN")
        return this
    }

    fun goBack(): BaseRobot {
        onView(withId(R.id.backButton)).perform(click())
        Log.d("TEST", "GO BACK")
        return this
    }
}
