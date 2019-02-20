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


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
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

        val movie1 = Movie(1, "", "Mock movie 1", "0", "0", "0", "")
        movies = Lists.newArrayList(movie1)

        val movieCached1 = Movie(1, "", "Mock cached movie 1", "0", "0", "0", "")
        moviesCached = Lists.newArrayList(movieCached1)
    }

    @After
    fun after() {
        closeKoin()
    }

    @Test
    fun getMoviesRemoteList_Success() {
        Mockito.`when`(mockNetManager.isConnectedToInternet).thenReturn(true)
        Mockito.`when`(mockMovieRemoteDataSource.getMovies("1", "")).thenReturn(Observable.just(MovieListResponse(1, movies)))

        viewModel.moviesScreenState.observeForever(mockObserver)

        viewModel.listMovies("")

        handleLoadingProperly()

        assertEquals("Invalid Status", ScreenStatus.OK.status, viewModel.moviesScreenState.value?.status)
        assertTrue("No data found", viewModel.moviesScreenState.value?.movies?.size ?: 0 > 0)
        assertEquals("Wrong list received","Mock movie 1", viewModel.moviesScreenState.value?.movies?.get(0)?.title)
    }

    @Test
    fun getMoviesLocalList_Success() {
        Mockito.`when`(mockNetManager.isConnectedToInternet).thenReturn(false)
        Mockito.`when`(mockMovieLocalDataSource.getMovies("1", "")).thenReturn(Observable.just(MovieListResponse(1, moviesCached)))

        viewModel.moviesScreenState.observeForever(mockObserver)

        viewModel.listMovies("")

        handleLoadingProperly()

        assertEquals("Invalid Status", ScreenStatus.OK.status,
                viewModel.moviesScreenState.value?.status)
        assertTrue("No data found",
                viewModel.moviesScreenState.value?.movies?.size ?: 0 > 0)
        assertEquals("Wrong list received","Mock cached movie 1",
                viewModel.moviesScreenState.value?.movies?.get(0)?.title)
    }

    @Test
    fun getMovieList_NoDataFound() {
        Mockito.`when`(mockNetManager.isConnectedToInternet).thenReturn(false)
        Mockito.`when`(mockMovieLocalDataSource.getMovies("1", ""))
                .thenReturn(Observable.just(MovieListResponse(1, ArrayList())))

        viewModel.moviesScreenState.observeForever(mockObserver)

        viewModel.listMovies("")

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
}
