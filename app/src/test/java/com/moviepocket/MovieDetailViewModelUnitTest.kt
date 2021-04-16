package com.moviepocket

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.moviepocket.di.mainModule
import com.moviepocket.features.movieDetail.model.data.MovieDetailRemoteDataSource
import com.moviepocket.features.movieDetail.model.data.MovieDetailRepository
import com.moviepocket.features.movieDetail.viewmodel.MovieDetailScreenState
import com.moviepocket.features.movieDetail.viewmodel.MovieDetailViewModel
import com.moviepocket.util.Constants
import io.reactivex.Observable
import io.reactivex.internal.schedulers.TrampolineScheduler
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.*
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.rules.TestRule
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
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

    private lateinit var screenStateLiveData: TestObserver<MovieDetailScreenState>

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)

        startKoin { modules(mainModule) }
        loadKoinModules(module {
            factory(override = true) { movieDetailRemoteDataSource }
            factory(override = true) { MovieDetailRepository(get()) }
        })

        configureMockServer()

        viewModel = MovieDetailViewModel(movieDetailRepository, TrampolineScheduler.instance(), TrampolineScheduler.instance())
        viewModel.movieDetailLiveData.observeForever(mockObserver)

        screenStateLiveData = viewModel.movieDetailLiveData.testObserver()
    }

    @After
    fun after() {
        stopKoin()
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
        screenStateLiveData.observedValues[0]?.apply {
            assertTrue("Should be loading", isLoading())
        }
    }

    private fun `assert that stops loading`() {
        screenStateLiveData.observedValues[1]?.apply {
            assertFalse("Should not be loading", isLoading())
        }
    }

    private fun `assert that handle loading properly`() {
        `assert that starts loading`()
        `assert that stops loading`()
    }

    private fun `assert that status OK is returned`() {
        screenStateLiveData.observedValues[1]?.apply {
            assertTrue("OK status expected", isStatusOk())
        }
    }

    private fun `assert that error status is returned`() {
        screenStateLiveData.observedValues[1]?.apply {
            assertTrue("OK status expected", isThereError())
        }
    }

    private fun configureMockServer() {
        server.start()
        Constants.BASE_URL = server.url("/").toString()
        server.enqueue(MockResponse().setResponseCode(200).setBody(JsonUtils.getJson("movie-detail.json")))
    }
}