package com.moviepocket

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.diego.mvvmwithscreenstates.JsonUtils
import com.moviepocket.features.movieDetail.data.MovieDetailRemoteDataSource
import com.moviepocket.features.movieDetail.data.MovieDetailRepository
import com.moviepocket.features.movieDetail.viewmodel.MovieDetailScreenState
import com.moviepocket.features.movieDetail.viewmodel.MovieDetailViewModel
import com.moviepocket.util.Constants
import io.reactivex.Observable
import io.reactivex.schedulers.TestScheduler
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.*
import org.junit.rules.TestRule
import org.koin.dsl.module.applicationContext
import org.koin.standalone.StandAloneContext
import org.koin.standalone.inject
import org.koin.test.KoinTest
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.Spy

class MovieDetailViewModelUnitTest : KoinTest {
    private val testScheduler = TestScheduler()
    private val mockObserver = mock<Observer<MovieDetailScreenState>>()
    private val server = MockWebServer()

    @Spy lateinit var movieDetailRemoteDataSource: MovieDetailRemoteDataSource
    private val movieDetailRepository: MovieDetailRepository by inject()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var viewModel: MovieDetailViewModel

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)

        StandAloneContext.loadKoinModules(listOf(applicationContext {
            bean { movieDetailRemoteDataSource }
            bean { MovieDetailRepository(get()) }
        }))

        configureMockServer()

        viewModel = MovieDetailViewModel(movieDetailRepository, testScheduler, testScheduler)
        viewModel.movieDetailLiveData.observeForever(mockObserver)
    }

    @After
    fun after() {
        StandAloneContext.closeKoin()
    }

    @Test
    fun `get movie detail success`() {
        viewModel.getMovieDetail("1")

        `assert that handle loading properly`()

        `assert that status OK is returned`()
    }

    @Test
    fun `get movie detail error`() {
        Mockito.`when`(movieDetailRepository.getMovieDetail(anyString()))
                .thenReturn(Observable.error(Throwable()))
        viewModel.getMovieDetail("1")

        `assert that handle loading properly`()

        `assert that error status is returned`()
    }

    private fun `assert that handle loading properly`() {
        viewModel.movieDetailLiveData.value?.isLoading()?.let {
            Assert.assertTrue("Is loading", it)
        }

        testScheduler.triggerActions()

        viewModel.movieDetailLiveData.value?.isLoading()?.let {
            Assert.assertFalse("Finished loading", it)
        }
    }

    private fun `assert that status OK is returned`() {
        Assert.assertTrue("OK status expected",
                viewModel.movieDetailLiveData.value?.isStatusOk() ?: false)
    }

    private fun `assert that error status is returned`() {
        Assert.assertTrue("Error status expected",
                viewModel.movieDetailLiveData.value?.isThereError() ?: false)
    }

    private fun configureMockServer() {
        server.start()
        Constants.BASE_URL = server.url("/").toString()
        server.enqueue(MockResponse().setResponseCode(200).setBody(JsonUtils.getJson("movie-detail.json")))
    }
}