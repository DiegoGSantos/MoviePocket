package com.moviepocket

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.google.common.collect.Lists
import com.moviepocket.features.moviesList.data.MovieLocalDataSource
import com.moviepocket.features.moviesList.data.MovieRemoteDataSource
import com.moviepocket.features.moviesList.data.MovieRepository
import com.moviepocket.features.moviesList.model.Movie
import com.moviepocket.features.moviesList.viewmodel.MovieListScreenState
import com.moviepocket.features.moviesList.viewmodel.MoviesViewModel
import com.moviepocket.features.moviesList.viewmodel.ScreenStatus
import com.moviepocket.manager.NetManager
import com.moviepocket.restclient.response.MovieListResponse
import io.reactivex.Observable
import io.reactivex.schedulers.TestScheduler
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
import org.mockito.Mockito.spy
import org.mockito.MockitoAnnotations

class MovieViewModelUnitTest : KoinTest {

    @Mock lateinit var mockNetManager: NetManager
    @Mock lateinit var mockMovieLocalDataSource: MovieLocalDataSource
    @Mock lateinit var mockMovieRemoteDataSource: MovieRemoteDataSource
    private val testScheduler = TestScheduler()
    private val mockObserver = mock<Observer<MovieListScreenState>>()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val mockRepository: MovieRepository by inject()
    private lateinit var viewModel: MoviesViewModel
    private lateinit var movies: ArrayList<Movie>
    private lateinit var moviesSecondPage: ArrayList<Movie>
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

        val movie1 = Movie(1, "", "Mock movie 1", "0", "0", "0", "")
        val movie2 = Movie(1, "", "Mock movie 2", "0", "0", "0", "")
        movies = Lists.newArrayList(movie1)
        moviesSecondPage = Lists.newArrayList(movie2)

        val movieCached1 = Movie(1, "", "Mock cached movie 1", "0", "0", "0", "")
        moviesCached = Lists.newArrayList(movieCached1)
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

        handleLoadingProperly()

        assertEquals("Invalid Status", ScreenStatus.ERROR.status, viewModel.moviesScreenState.value?.status)

        assertTrue("Should not receive data",
                viewModel.moviesScreenState.value?.movies?.size ?: 0 == 0)
    }

    @Test
    fun `starting view with remote`() {
        Mockito.`when`(mockNetManager.isConnectedToInternet).thenReturn(true)
        Mockito.`when`(mockMovieRemoteDataSource.getMovies("1", "")).thenReturn(Observable.just(MovieListResponse(1, movies)))

        viewModel.onViewCreated("")

        handleLoadingProperly()

        assertEquals("Invalid Status", ScreenStatus.OK.status, viewModel.moviesScreenState.value?.status)

        assertTrue("No data found", viewModel.moviesScreenState.value?.movies?.size ?: 0 > 0)

        `assert list comes from remote`()
    }

    @Test
    fun `restarting view with remote`() {
        Mockito.`when`(mockNetManager.isConnectedToInternet).thenReturn(true)
        Mockito.`when`(mockMovieRemoteDataSource.getMovies("1", "")).thenReturn(Observable.just(MovieListResponse(1, movies)))

        viewModel.onViewCreated("")

        handleLoadingProperly()

        assertEquals("Invalid Status", ScreenStatus.OK.status, viewModel.moviesScreenState.value?.status)

        assertTrue("No data found", viewModel.moviesScreenState.value?.movies?.size ?: 0 > 0)

        `assert list comes from remote`()

        viewModel.onViewCreated("")

        assertEquals("Invalid Status", ScreenStatus.OK.status, viewModel.moviesScreenState.value?.status)

        assertTrue("No data found", viewModel.moviesScreenState.value?.movies?.size ?: 0 > 0)

        `assert list comes from remote`()
    }

    @Test
    fun `asking new page with remote`() {
        Mockito.`when`(mockNetManager.isConnectedToInternet).thenReturn(true)
        Mockito.`when`(mockMovieRemoteDataSource.getMovies("1", "")).thenReturn(Observable.just(MovieListResponse(1, movies)))

        viewModel.onNewPageRequested("")

        testScheduler.triggerActions()

        assertEquals("Invalid Status", ScreenStatus.OK.status, viewModel.moviesScreenState.value?.status)

        assertTrue("No data found", viewModel.moviesScreenState.value?.movies?.size ?: 0 > 0)

        `assert list comes from remote`()
    }

    @Test
    fun `starting view and asking new page with remote`() {
        Mockito.`when`(mockNetManager.isConnectedToInternet).thenReturn(true)
        Mockito.`when`(mockMovieRemoteDataSource.getMovies("1", "")).thenReturn(Observable.just(MovieListResponse(2, movies)))
        Mockito.`when`(mockMovieRemoteDataSource.getMovies("2", "")).thenReturn(Observable.just(MovieListResponse(2, moviesSecondPage)))

        viewModel.onViewCreated("")

        testScheduler.triggerActions()

        assertEquals("Invalid Status", ScreenStatus.OK.status, viewModel.moviesScreenState.value?.status)

        assertTrue("No data found", viewModel.moviesScreenState.value?.movies?.size ?: 0 > 0)

        `assert list comes from remote`()

        viewModel.onNewPageRequested("")

        testScheduler.triggerActions()

        assertEquals("Invalid Status", ScreenStatus.OK.status, viewModel.moviesScreenState.value?.status)

        assertTrue("Second page not loaded", viewModel.moviesScreenState.value?.movies?.size ?: 0 > 0)

        `assert list comes from second page remote`()
    }

    @Test
    fun `starting view and asking new page when there is none available`() {
        Mockito.`when`(mockNetManager.isConnectedToInternet).thenReturn(true)
        Mockito.`when`(mockMovieRemoteDataSource.getMovies("1", "")).thenReturn(Observable.just(MovieListResponse(1, movies)))
        Mockito.`when`(mockMovieRemoteDataSource.getMovies("2", "")).thenReturn(Observable.just(MovieListResponse(1, moviesSecondPage)))

        viewModel.onViewCreated("")

        testScheduler.triggerActions()

        assertEquals("Invalid Status", ScreenStatus.OK.status, viewModel.moviesScreenState.value?.status)

        assertTrue("No data found", viewModel.moviesScreenState.value?.movies?.size ?: 0 > 0)

        `assert list comes from remote`()

        viewModel.onNewPageRequested("")

        testScheduler.triggerActions()

        assertEquals("Invalid Status", ScreenStatus.OK.status, viewModel.moviesScreenState.value?.status)
    }

    @Test
    fun `starting view with cache`() {
        Mockito.`when`(mockNetManager.isConnectedToInternet).thenReturn(false)
        Mockito.`when`(mockMovieLocalDataSource.getMovies("1", "")).thenReturn(Observable.just(MovieListResponse(1, moviesCached)))

        viewModel.onViewCreated("")

        handleLoadingProperly()

        assertEquals("Invalid Status", ScreenStatus.OK.status,
                viewModel.moviesScreenState.value?.status)

        assertTrue("No data found",
                viewModel.moviesScreenState.value?.movies?.size ?: 0 > 0)

        `assert list comes from cache`()
    }

    @Test
    fun `asking new page with cache`() {
        Mockito.`when`(mockNetManager.isConnectedToInternet).thenReturn(false)
        Mockito.`when`(mockMovieLocalDataSource.getMovies("1", "")).thenReturn(Observable.just(MovieListResponse(1, moviesCached)))

        viewModel.onNewPageRequested("")

        handleLoadingProperly()

        assertEquals("Invalid Status", ScreenStatus.OK.status,
                viewModel.moviesScreenState.value?.status)

        assertTrue("No data found",
                viewModel.moviesScreenState.value?.movies?.size ?: 0 > 0)

        `assert list comes from cache`()
    }

    @Test
    fun `starting view no data found`() {
        Mockito.`when`(mockNetManager.isConnectedToInternet).thenReturn(false)
        Mockito.`when`(mockMovieLocalDataSource.getMovies("1", ""))
                .thenReturn(Observable.just(MovieListResponse(1, ArrayList())))

        viewModel.onViewCreated("")

        handleLoadingProperly()

        assertEquals("Invalid Status", ScreenStatus.NO_DATA_FOUND.status,
                viewModel.moviesScreenState.value?.status)

        assertTrue("Should not receive data",
                viewModel.moviesScreenState.value?.movies?.size ?: 0 == 0)
    }

    private fun handleLoadingProperly() {
        viewModel.moviesScreenState.value?.isLoading()?.let {
            assertTrue("Is loading", it)
        }

        testScheduler.triggerActions()

        viewModel.moviesScreenState.value?.isLoading()?.let {
            assertFalse("Finished loading", it)
        }
    }

    private fun `assert list comes from remote`() {
        assertEquals("Wrong list received", "Mock movie 1", viewModel.moviesScreenState.value?.movies?.get(0)?.title)
    }

    private fun `assert list comes from second page remote`() {
        assertEquals("Wrong list received", "Mock movie 2", viewModel.moviesScreenState.value?.movies?.get(0)?.title)
    }

    private fun `assert list comes from cache`() {
        assertEquals("Wrong list received", "Mock cached movie 1",
                viewModel.moviesScreenState.value?.movies?.get(0)?.title)
    }
}
