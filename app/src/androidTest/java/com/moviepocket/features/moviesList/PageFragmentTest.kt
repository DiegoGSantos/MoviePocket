package com.moviepocket

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.diego.tweetssentimentsanalyzer.base.BaseRobot
import com.moviepocket.base.BaseTest
import com.moviepocket.features.movieDetail.view.MovieDetailActivity
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PageFragmentTest : BaseTest() {

    @Test
    fun testShowMovieDetail() {
        test()
//
//        waitLoading()
//
//        robot.assertItTakeMeToScreen(MovieDetailActivity::class.java)
//        .goBack()
    }

    @Test
    fun testShowMovieDetail1() {
        test()
    }

    @Test
    fun testShowMovieDetail2() {
        test()
    }

    @Test
    fun testShowMovieDetail3() {
        test()
    }

    @Test
    fun testShowMovieDetail4() {
        test()
    }

    @Test
    fun testShowMovieDetail5() {
        test()
    }

    private fun test() {
        val robot = BaseRobot()

//        Thread.sleep(1000)
        robot
                .clickRecyclerViewItem(R.id.moviesList, 0, R.id.container)

//        Thread.sleep(1000)

        robot.assertItTakeMeToScreen(MovieDetailActivity::class.java)
        .goBack()
    }
}
