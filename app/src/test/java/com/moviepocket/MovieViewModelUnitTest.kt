package com.moviepocket

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import android.widget.ImageView
import com.diego.mvvmwithscreenstates.JsonUtils.Companion.getJson
import com.google.common.collect.Lists
import com.moviepocket.features.Event
import com.moviepocket.features.moviesList.model.data.MovieLocalDataSource
import com.moviepocket.features.moviesList.model.data.MovieRemoteDataSource
import com.moviepocket.features.moviesList.model.data.MovieRepository
import com.moviepocket.features.moviesList.model.domain.Movie
import com.moviepocket.features.moviesList.viewmodel.MovieListScreenEvent
import com.moviepocket.features.moviesList.viewmodel.MovieListScreenState
import com.moviepocket.features.moviesList.viewmodel.MoviesViewModel
import com.moviepocket.manager.NetManager
import com.moviepocket.restclient.response.MovieListResponse
import com.moviepocket.util.Constants
import io.reactivex.Observable
import io.reactivex.internal.schedulers.TrampolineScheduler
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
import org.mockito.MockitoAnnotations

class MovieViewModelUnitTest : KoinTest {

    @Mock lateinit var mockNetManager: NetManager
    @Mock lateinit var mockMovieLocalDataSource: MovieLocalDataSource
    @Mock lateinit var mockMovieRemoteDataSource: MovieRemoteDataSource
    private val mockObserver = mock<Observer<MovieListScreenState>>()
    private val server = MockWebServer()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val mockRepository: MovieRepository by inject()
    private lateinit var viewModel: MoviesViewModel
    private lateinit var moviesCached: ArrayList<Movie>

    private lateinit var screenEventLiveData: TestObserver<Event<MovieListScreenEvent>>
    private lateinit var screenStateLiveData: TestObserver<MovieListScreenState>

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)

        loadKoinModules(listOf(applicationContext {
            bean { mockNetManager }
            bean { mockMovieRemoteDataSource }
            bean { mockMovieLocalDataSource }
            bean { MovieRepository(get(), get(), get()) }
        }))

        viewModel = MoviesViewModel(mockRepository, TrampolineScheduler.instance(), TrampolineScheduler.instance(), mockNetManager)
        viewModel.moviesScreenState.observeForever(mockObserver)

        val movieCached1 = Movie(1, "", "Mock cached movie 1", "0", "0", "0", "")
        moviesCached = Lists.newArrayList(movieCached1)

        Mockito.`when`(mockMovieRemoteDataSource.getMovies(anyString(), anyString())).thenCallRealMethod()
        configureMockServer()

        screenEventLiveData = viewModel.movieScreenEvent.testObserver()
        screenStateLiveData = viewModel.moviesScreenState.testObserver()
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

        `assert that status OK is returned`()

        `assert that list returned is not empty`()

        `assert that list comes from remote`()
    }

    @Test
    fun `starting view and asking new page with remote`() {
        Mockito.`when`(mockNetManager.isConnectedToInternet).thenReturn(true)

        viewModel.onViewCreated("")

        `assert that status OK is returned`()

        `assert that list returned is not empty`()

        `assert that list comes from remote`()

        viewModel.onNewPageRequested("")

        `assert that status OK is returned`()

        `assert list comes from second page remote`()
    }

    @Test
    fun `starting view and asking new page when there is none available`() {
        Mockito.`when`(mockNetManager.isConnectedToInternet).thenReturn(true)

        viewModel.onViewCreated("")

        `assert that handle loading properly`()

        `assert that status OK is returned`()

        `assert that list returned is not empty`()

        `assert that list comes from remote`()

        viewModel.onNewPageRequested("")

        `assert that status OK is returned`()

        `assert list comes from second page remote`()

        viewModel.onNewPageRequested("")

        `assert that nothing is sent`()
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

    @Test
    fun `assert that it opens movie detail`() {
        Mockito.`when`(mockNetManager.isConnectedToInternet).thenReturn(true)
        viewModel.onMovieClicked(Movie())

        `assert that it sends openMovieDetail screen event`()
    }

    @Test
    fun `assert that it shows connectivity error when opening detail with no internet`() {
        Mockito.`when`(mockNetManager.isConnectedToInternet).thenReturn(false)
        viewModel.onMovieClicked(Movie())

        `assert that it sends error screen event`()
    }

    @Test
    fun `assert that it opens movie preview`() {
        Mockito.`when`(mockNetManager.isConnectedToInternet).thenReturn(true)
        viewModel.onMovieLongClicked(Movie())

        `assert that it sends show preview screen event`()
    }

    @Test
    fun `assert that it shows connectivity error when opening preview with no internet`() {
        Mockito.`when`(mockNetManager.isConnectedToInternet).thenReturn(false)
        viewModel.onMovieLongClicked(Movie())

        `assert that it sends error screen event`()
    }

    private fun configureMockServer() {
        server.start()
        Constants.BASE_URL = server.url("/").toString()

        val dispatcher = object : Dispatcher() {

            @Throws(InterruptedException::class)
            override fun dispatch(request: RecordedRequest): MockResponse {
                return when {
                    request.path!!.contains("/movie") && request.path!!.contains("page=1") ->
                        MockResponse().setResponseCode(200).setBody(getJson("movies-page-1.json"))
                    request.path!!.contains("/movie") && request.path!!.contains("page=2") ->
                        MockResponse().setResponseCode(200).setBody(getJson("movies-page-2.json"))
                    else -> MockResponse().setResponseCode(404)
                }
            }
        }

        server.dispatcher = dispatcher
    }

    private fun `assert no data status is returned`() {
        screenStateLiveData.observedValues[1]?.apply {
            assertTrue("No data status expected", isDataNotAvailable())
        }
    }

    private fun `assert that handle loading properly`() {
        `assert that starts loading`()

        `assert that stops loading`()
    }

    private fun `assert that list comes from remote`() {
        screenStateLiveData.observedValues[1]?.apply {
            assertEquals("List should come from remote", "Capitã Marvel",
                    movies[0].title)
        }
    }

    private fun `assert list comes from second page remote`() {
        screenStateLiveData.observedValues[2]?.apply {
            assertEquals("Second page should come from remote", "Cinderela Pop",
                    movies[0].title)
        }
    }

    private fun `assert list comes from cache`() {
        screenStateLiveData.observedValues[1]?.apply {
            assertEquals("List should comer from cache", "Mock cached movie 1",
                    movies[0].title)
        }
    }

    private fun `assert that status OK is returned`() {
        screenStateLiveData.observedValues[1]?.apply {
            assertTrue("OK status expected",
                    isStatusOk())
        }
    }

    private fun `assert that empty list is returned`() {
        screenStateLiveData.observedValues[1]?.apply {
            assertTrue("Should not receive data",
                    movies.isEmpty())
        }
    }

    private fun `assert that error status is returned`() {
        screenStateLiveData.observedValues[1]?.apply {
            assertTrue("Error status expected", isThereError())
        }
    }

    private fun `assert that list returned is not empty`() {
        screenStateLiveData.observedValues[1]?.apply {
            assertTrue("No data found", movies.isNotEmpty())
        }
    }

    private fun `assert that it sends openMovieDetail screen event`() {
        screenEventLiveData.observedValues[0]?.apply {
            assertTrue("OpenMovieDetail screen effect expected", getContentIfNotHandled() is MovieListScreenEvent.OpenMovieDetail)
        }
    }

    private fun `assert that it sends error screen event`() {
        screenEventLiveData.observedValues[0]?.apply {
            assertTrue("Error screen event expected", getContentIfNotHandled() is MovieListScreenEvent.Error)
        }
    }

    private fun `assert that it sends show preview screen event`() {
        screenEventLiveData.observedValues[0]?.apply {
            assertTrue("OpenMoviePreview screen effect expected", getContentIfNotHandled() is MovieListScreenEvent.OpenMoviePreview)
        }
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

    private fun `assert that nothing is sent`() {
        assertTrue("Should not send anything", screenStateLiveData.observedValues.size < 4)
    }
}
