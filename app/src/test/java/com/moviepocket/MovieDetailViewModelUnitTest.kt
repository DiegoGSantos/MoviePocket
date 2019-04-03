package com.moviepocket

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.diego.mvvmwithscreenstates.JsonUtils
import com.moviepocket.features.movieDetail.model.data.MovieDetailRemoteDataSource
import com.moviepocket.features.movieDetail.model.data.MovieDetailRepository
import com.moviepocket.features.movieDetail.viewmodel.MovieDetailScreenState
import com.moviepocket.features.movieDetail.viewmodel.MovieDetailViewModel
import com.moviepocket.util.Constants
import io.reactivex.Observable
import io.reactivex.internal.schedulers.TrampolineScheduler
import junit.framework.Assert.assertTrue
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
    private val mockObserver = mock<Observer<MovieDetailScreenState>>()
    private val server = MockWebServer()

    @Spy lateinit var movieDetailRemoteDataSource: MovieDetailRemoteDataSource
    private val movieDetailRepository: MovieDetailRepository by inject()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var viewModel: MovieDetailViewModel

    private lateinit var screenStatetLiveData: TestObserver<MovieDetailScreenState>

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)

        StandAloneContext.loadKoinModules(listOf(applicationContext {
            bean { movieDetailRemoteDataSource }
            bean { MovieDetailRepository(get()) }
        }))

        configureMockServer()

        viewModel = MovieDetailViewModel(movieDetailRepository, TrampolineScheduler.instance(), TrampolineScheduler.instance())
        viewModel.movieDetailLiveData.observeForever(mockObserver)

        screenStatetLiveData = viewModel.movieDetailLiveData.testObserver()
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

    private fun `assert that starts loading`() {
        screenStatetLiveData.observedValues[0]?.apply {
            Assert.assertTrue("Should be loading", isLoading())
        }
    }

    private fun `assert that stops loading`() {
        screenStatetLiveData.observedValues[1]?.apply {
            Assert.assertFalse("Should not be loading", isLoading())
        }
    }

    private fun `assert that handle loading properly`() {
        `assert that starts loading`()
        `assert that stops loading`()
    }

    private fun `assert that status OK is returned`() {
        screenStatetLiveData.observedValues[1]?.apply {
            assertTrue("OK status expected", isStatusOk())
        }
    }

    private fun `assert that error status is returned`() {
        screenStatetLiveData.observedValues[1]?.apply {
            assertTrue("OK status expected", isThereError())
        }
    }

    private fun configureMockServer() {
        server.start()
        Constants.BASE_URL = server.url("/").toString()
        server.enqueue(MockResponse().setResponseCode(200).setBody(JsonUtils.getJson("movie-detail.json")))
    }
}