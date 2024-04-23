package com.dicoding.storyapp

import androidx.paging.ExperimentalPagingApi
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.dicoding.storyapp.data.retrofit.ApiConfig
import com.dicoding.storyapp.ui.SplashScreenActivity
import com.dicoding.storyapp.ui.login.LoginActivity
import com.dicoding.storyapp.util.EspressoIdlingResource
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith



@ExperimentalPagingApi
@RunWith(AndroidJUnit4::class)
@LargeTest
class LoginLogoutTest {

    private val mockWebServer = MockWebServer()

    @get:Rule
    val activityRule = ActivityScenarioRule(SplashScreenActivity::class.java)

    @Before
    fun setUp() {
        mockWebServer.start(8080)
        ApiConfig.BASE_URL = "http://127.0.0.1:8080/"
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun loginLogout() {
        Thread.sleep(5000L)

        onView(withId(R.id.ed_login_email)).perform(typeText("joncena@gmail.com"))
        onView(withId(R.id.ed_login_password)).perform(typeText("12345678"))
        onView(withId(R.id.loginButton)).perform(click())

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(JsonConverter.readStringFromFile("success_response.json"))
        mockWebServer.enqueue(mockResponse)

        Thread.sleep(5000L)
        onView(withId(R.id.settings)).perform(click())

        Thread.sleep(2000L)
        Intents.init()
        onView(withId(R.id.action_logout)).perform(click())

        Thread.sleep(2000L)
        intended(hasComponent(LoginActivity::class.java.name))
    }
}