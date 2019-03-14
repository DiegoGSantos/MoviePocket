package com.moviepocket

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.diego.mvvmwithscreenstates.JsonUtils.Companion.getJson
import com.google.common.collect.Lists
import com.moviepocket.features.moviesList.data.MovieLocalDataSource
import com.moviepocket.features.moviesList.data.MovieRemoteDataSource
import com.moviepocket.features.moviesList.data.MovieRepository
import com.moviepocket.features.moviesList.model.Movie
import com.moviepocket.features.moviesList.viewmodel.MovieListScreenState
import com.moviepocket.features.moviesList.viewmodel.MoviesViewModel
import com.moviepocket.manager.NetManager
import com.moviepocket.restclient.response.MovieListResponse
import com.moviepocket.util.Constants
import io.reactivex.Observable
import io.reactivex.schedulers.TestScheduler
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.koin.dsl.module.applicationContext
import org.koin.standalone.StandAloneContext.closeKoin
import org.koin.standalone.StandAloneContext.loadKoinModules
import org.koin.standalone.inject
import org.koin.test.KoinTest
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.anyString
import org.mockito.Mockito.spy
import org.mockito.MockitoAnnotations

class MovieViewModelUnitTest : KoinTest {

    @Mock lateinit var mockNetManager: NetManager
    @Mock lateinit var mockMovieLocalDataSource: MovieLocalDataSource
    @Mock lateinit var mockMovieRemoteDataSource: MovieRemoteDataSource
    private val testScheduler = TestScheduler()
    private val mockObserver = mock<Observer<MovieListScreenState>>()
    private val server = MockWebServer()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val mockRepository: MovieRepository by inject()
    private lateinit var viewModel: MoviesViewModel
    private lateinit var moviesCached: ArrayList<Movie>

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)

        loadKoinModules(listOf(applicationContext {
            bean { mockNetManager }
            bean { mockMovieRemoteDataSource }
            bean { mockMovieLocalDataSource }
            bean { MovieRepository(get(), get(), get()) }
        }))

        viewModel = spy(MoviesViewModel(mockRepository, testScheduler, testScheduler))
        viewModel.moviesScreenState.observeForever(mockObserver)

        val movieCached1 = Movie(1, "", "Mock cached movie 1", "0", "0", "0", "")
        moviesCached = Lists.newArrayList(movieCached1)

        Mockito.`when`(mockMovieRemoteDataSource.getMovies(anyString(), anyString())).thenCallRealMethod()
        configureMockServer()
    }

    @After
    fun after() {
        closeKoin()
    }

    @Test
    fun `starting view with error`() {
        Mockito.`when`(mockNetManager.isConnectedToInternet).thenReturn(true)
        Mockito.`when`(mockMovieRemoteDataSource.getMovies("1", "")).thenReturn(Observable.error(Throwable()))

        viewModel.onViewCreated("")

        `assert that handle loading properly`()

        `assert that error status is returned`()

        `assert that empty list is returned`()
    }

    @Test
    fun `starting view with remote`() {
        Mockito.`when`(mockNetManager.isConnectedToInternet).thenReturn(true)

        viewModel.onViewCreated("")

        `assert that handle loading properly`()

        `assert that status OK is returned`()

        `assert that list returned is not empty`()

        `assert that list comes from remote`()
    }

    @Test
    fun `restarting view with remote`() {
        Mockito.`when`(mockNetManager.isConnectedToInternet).thenReturn(true)

        viewModel.onViewCreated("")

        `assert that handle loading properly`()

        `assert that status OK is returned`()

        `assert that list returned is not empty`()

        `assert that list comes from remote`()

        viewModel.onViewCreated("")

        `assert that status OK is returned`()

        `assert that list returned is not empty`()

        `assert that list comes from remote`()
    }

    @Test
    fun `asking new page with remote`() {
        Mockito.`when`(mockNetManager.isConnectedToInternet).thenReturn(true)

        viewModel.onNewPageRequested("")

        testScheduler.triggerActions()

        `assert that status OK is returned`()

        `assert that list returned is not empty`()

        `assert that list comes from remote`()
    }

    @Test
    fun `starting view and asking new page with remote`() {
        Mockito.`when`(mockNetManager.isConnectedToInternet).thenReturn(true)

        viewModel.onViewCreated("")

        testScheduler.triggerActions()

        `assert that status OK is returned`()

        `assert that list returned is not empty`()

        `assert that list comes from remote`()

        viewModel.onNewPageRequested("")

        testScheduler.triggerActions()

        `assert that status OK is returned`()

        `check second page was returned`()

        `assert list comes from second page remote`()
    }

    @Test
    fun `starting view and asking new page when there is none available`() {
        Mockito.`when`(mockNetManager.isConnectedToInternet).thenReturn(true)

        viewModel.onViewCreated("")

        testScheduler.triggerActions()

        `assert that status OK is returned`()

        `assert that list returned is not empty`()

        `assert that list comes from remote`()

        viewModel.onNewPageRequested("")

        testScheduler.triggerActions()

        `assert that status OK is returned`()
    }

    @Test
    fun `starting view with cache`() {
        Mockito.`when`(mockNetManager.isConnectedToInternet).thenReturn(false)
        Mockito.`when`(mockMovieLocalDataSource.getMovies("1", "")).thenReturn(Observable.just(MovieListResponse(1, moviesCached)))

        viewModel.onViewCreated("")

        `assert that handle loading properly`()

        `assert that status OK is returned`()

        `assert that list returned is not empty`()

        `assert list comes from cache`()
    }

    @Test
    fun `asking new page with cache`() {
        Mockito.`when`(mockNetManager.isConnectedToInternet).thenReturn(false)
        Mockito.`when`(mockMovieLocalDataSource.getMovies("1", "")).thenReturn(Observable.just(MovieListResponse(1, moviesCached)))

        viewModel.onNewPageRequested("")

        `assert that handle loading properly`()

        `assert that status OK is returned`()

        `assert that list returned is not empty`()

        `assert list comes from cache`()
    }

    @Test
    fun `starting view no data found`() {
        Mockito.`when`(mockNetManager.isConnectedToInternet).thenReturn(false)
        Mockito.`when`(mockMovieLocalDataSource.getMovies("1", ""))
                .thenReturn(Observable.just(MovieListResponse(1, ArrayList())))

        viewModel.onViewCreated("")

        `assert that handle loading properly`()

        `assert no data status is returned`()

        `assert that empty list is returned`()
    }

    private fun configureMockServer() {
        server.start()
        Constants.BASE_URL = server.url("/").toString()

        val dispatcher = object : Dispatcher() {

            @Throws(InterruptedException::class)
            override fun dispatch(request: RecordedRequest): MockResponse {

                val test = getJson("movies-page-1.json")

                return when {
                    request.path.contains("/movie") && request.path.contains("page=1") ->
                        MockResponse().setResponseCode(200).setBody(getJson("movies-page-1.json"))
                    request.path.contains("/movie") && request.path.contains("page=2") ->
                        MockResponse().setResponseCode(200).setBody(getJson("movies-page-2.json"))
                    else -> MockResponse().setResponseCode(404)
                }
            }
        }

        server.setDispatcher(dispatcher)
    }

    private fun `assert no data status is returned`() {
        viewModel.moviesScreenState.value?.isDataNotAvailable()?.let {
            assertTrue("No data status expected", it)
        }
    }

    private fun `assert that handle loading properly`() {
        viewModel.moviesScreenState.value?.isLoading()?.let {
            assertTrue("Is loading", it)
        }

        testScheduler.triggerActions()

        viewModel.moviesScreenState.value?.isLoading()?.let {
            assertFalse("Finished loading", it)
        }
    }

    private fun `assert that list comes from remote`() {
        assertEquals("Wrong list received", "Capitã Marvel", viewModel.moviesScreenState.value?.movies?.get(0)?.title)
    }

    private fun `assert list comes from second page remote`() {
        assertEquals("Wrong list received", "Cinderela Pop", viewModel.moviesScreenState.value?.movies?.get(0)?.title)
    }

    private fun `assert list comes from cache`() {
        assertEquals("Wrong list received", "Mock cached movie 1",
                viewModel.moviesScreenState.value?.movies?.get(0)?.title)
    }

    private fun `assert that status OK is returned`() {
        assertTrue("OK status expected",
                viewModel.moviesScreenState.value?.isStatusOk() ?: false)
    }

    private fun `assert that empty list is returned`() {
        assertTrue("Should not receive data",
                viewModel.moviesScreenState.value?.movies?.size ?: 0 == 0)
    }

    private fun `assert that error status is returned`() {
        assertTrue("Error status expected", viewModel.moviesScreenState.value?.isThereError() ?: false)
    }

    private fun `check second page was returned`() {
        assertTrue("Second page not loaded", viewModel.moviesScreenState.value?.movies?.size ?: 0 > 0)
    }

    private fun `assert that list returned is not empty`() {
        assertTrue("No data found", viewModel.moviesScreenState.value?.movies?.size ?: 0 > 0)
    }
}
