package com.moviepocket.base

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.content.Intent
import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.intent.Intents
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import com.diego.mvvmwithscreenstates.JsonFileUtils
import com.moviepocket.R
import com.moviepocket.features.moviesList.view.MainActivity
import com.moviepocket.util.Constants
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import org.hamcrest.Matchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestRule


open class BaseTest {
//    @get:Rule
//    val intentsTestRule = IntentsTestRule(MainActivity::class.java, true, false)

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java, false, false)

    //start mock web server
    @Rule @JvmField
    val mMockServerRule = MockServerRule()

    internal fun waitLoading() {
        //TODO replace with IdlingResource
        Thread.sleep(500)
        while (isLoadingVisible()) {
            Thread.sleep(500)
        }

    }

    private fun isLoadingVisible(): Boolean {
        try {
            onView(allOf(withId(R.id.loadingProgress), isDisplayed()))
                    .check(ViewAssertions.matches(isDisplayed()))
            return true
        } catch (ignored: Throwable) {
        }
        return false
    }

    @Before
    fun setUp() {
        Constants.BASE_URL = mMockServerRule.server().url("/").toString()

        val dispatcher = object : Dispatcher() {

            @Throws(InterruptedException::class)
            override fun dispatch(request: RecordedRequest): MockResponse {
                return when {
                    request.path.contains("/movie") && request.path.contains("page=1") ->
                        MockResponse().setResponseCode(200).setBody(
                                "{\n" +
                                "   \"total_pages\":1,\n" +
                                "   \"results\":[\n" +
                                "      {\n" +
                                "         \"vote_count\":1580,\n" +
                                "         \"id\":299537,\n" +
                                "         \"video\":false,\n" +
                                "         \"vote_average\":7.3,\n" +
                                "         \"title\":\"Capitã Marvel\",\n" +
                                "         \"popularity\":611.128,\n" +
                                "         \"poster_path\":\"\\/hVgLHgnsO46oSHJy5I4ekhqtoYv.jpg\",\n" +
                                "         \"original_language\":\"en\",\n" +
                                "         \"original_title\":\"Captain Marvel\",\n" +
                                "         \"genre_ids\":[\n" +
                                "            28,\n" +
                                "            12,\n" +
                                "            878,\n" +
                                "            36\n" +
                                "         ],\n" +
                                "         \"backdrop_path\":\"\\/w2PMyoyLU22YvrGK3smVM9fW1jj.jpg\",\n" +
                                "         \"adult\":false,\n" +
                                "         \"overview\":\"A história segue Carol Danvers e como ela se torna um dos heróis mais poderosos do universo quando a Terra é pega no meio de uma guerra galáctica entre duas raças alienígenas. Situado na década de 1990, Capitã Marvel é uma nova aventura de um período inédito na história do universo cinematográfico da Marvel.\",\n" +
                                "         \"release_date\":\"2019-03-06\"\n" +
                                "      }\n" +
                                "   ]\n" +
                                "}")
                    request.path.contains("/movie") && request.path.contains("page=2") ->
                        MockResponse().setResponseCode(200).setBody(JsonFileUtils.getJson("movies-page-2.json"))
                    request.path.contains("/movie/299537") ->
                        MockResponse().setResponseCode(200).setBody(
                                "{\n" +
                                        "  \"adult\":false,\n" +
                                        "  \"backdrop_path\":\"/w2PMyoyLU22YvrGK3smVM9fW1jj.jpg\",\n" +
                                        "  \"belongs_to_collection\":null,\n" +
                                        "  \"budget\":152000000,\n" +
                                        "  \"genres\":[\n" +
                                        "    {\n" +
                                        "      \"id\":28,\n" +
                                        "      \"name\":\"Ação\"\n" +
                                        "    },\n" +
                                        "    {\n" +
                                        "      \"id\":12,\n" +
                                        "      \"name\":\"Aventura\"\n" +
                                        "    },\n" +
                                        "    {\n" +
                                        "      \"id\":878,\n" +
                                        "      \"name\":\"Ficção científica\"\n" +
                                        "    },\n" +
                                        "    {\n" +
                                        "      \"id\":36,\n" +
                                        "      \"name\":\"História\"\n" +
                                        "    }\n" +
                                        "  ],\n" +
                                        "  \"homepage\":\"https://www.marvel.com/movies/captain-marvel\",\n" +
                                        "  \"id\":299537,\n" +
                                        "  \"imdb_id\":\"tt4154664\",\n" +
                                        "  \"original_language\":\"en\",\n" +
                                        "  \"original_title\":\"Captain Marvel\",\n" +
                                        "  \"overview\":\"A história segue Carol Danvers e como ela se torna um dos heróis mais poderosos do universo quando a Terra é pega no meio de uma guerra galáctica entre duas raças alienígenas. Situado na década de 1990, Capitã Marvel é uma nova aventura de um período inédito na história do universo cinematográfico da Marvel.\",\n" +
                                        "  \"popularity\":553.057,\n" +
                                        "  \"poster_path\":\"/hVgLHgnsO46oSHJy5I4ekhqtoYv.jpg\",\n" +
                                        "  \"production_companies\":[\n" +
                                        "    {\n" +
                                        "      \"id\":420,\n" +
                                        "      \"logo_path\":\"/hUzeosd33nzE5MCNsZxCGEKTXaQ.png\",\n" +
                                        "      \"name\":\"Marvel Studios\",\n" +
                                        "      \"origin_country\":\"US\"\n" +
                                        "    }\n" +
                                        "  ],\n" +
                                        "  \"production_countries\":[\n" +
                                        "    {\n" +
                                        "      \"iso_3166_1\":\"US\",\n" +
                                        "      \"name\":\"United States of America\"\n" +
                                        "    }\n" +
                                        "  ],\n" +
                                        "  \"release_date\":\"2019-03-06\",\n" +
                                        "  \"revenue\":456285175,\n" +
                                        "  \"runtime\":124,\n" +
                                        "  \"spoken_languages\":[\n" +
                                        "    {\n" +
                                        "      \"iso_639_1\":\"en\",\n" +
                                        "      \"name\":\"English\"\n" +
                                        "    }\n" +
                                        "  ],\n" +
                                        "  \"status\":\"Released\",\n" +
                                        "  \"tagline\":\"Mais alto. Mais longe. Mais rápido.\",\n" +
                                        "  \"title\":\"Capitã Marvel\",\n" +
                                        "  \"video\":false,\n" +
                                        "  \"vote_average\":7.3,\n" +
                                        "  \"vote_count\":1627,\n" +
                                        "  \"release_dates\":{\n" +
                                        "    \"results\":[\n" +
                                        "      {\n" +
                                        "        \"iso_3166_1\":\"BR\",\n" +
                                        "        \"release_dates\":[\n" +
                                        "          {\n" +
                                        "            \"certification\":\"12\",\n" +
                                        "            \"iso_639_1\":\"\",\n" +
                                        "            \"note\":\"\",\n" +
                                        "            \"release_date\":\"2019-03-07T00:00:00.000Z\",\n" +
                                        "            \"type\":3\n" +
                                        "          }\n" +
                                        "        ]\n" +
                                        "      }\n" +
                                        "    ]\n" +
                                        "  },\n" +
                                        "  \"videos\":{\n" +
                                        "    \"results\":[\n" +
                                        "      {\n" +
                                        "        \"id\":\"5c8473fdc3a3684e8fdb9567\",\n" +
                                        "        \"iso_639_1\":\"pt\",\n" +
                                        "        \"iso_3166_1\":\"BR\",\n" +
                                        "        \"key\":\"FV7AxLbHcrE\",\n" +
                                        "        \"name\":\"Trailer Capitã Marvel - 07 de março nos cinemas\",\n" +
                                        "        \"site\":\"YouTube\",\n" +
                                        "        \"size\":1080,\n" +
                                        "        \"type\":\"Trailer\"\n" +
                                        "      }\n" +
                                        "    ]\n" +
                                        "  }\n" +
                                        "}"
                        )
                    else -> MockResponse().setResponseCode(404)
                }
            }
        }

        mMockServerRule.server().setDispatcher(dispatcher)

        val grouchyIntent = Intent()
        activityRule.launchActivity(grouchyIntent)

        Intents.init()
    }
}