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
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.spy
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class MovieViewModelUnitTest {

    @Mock lateinit var mockNetManager: NetManager
    @Mock lateinit var mockMovieLocalDataSource: MovieLocalDataSource
    @Mock lateinit var mockMovieRemoteDataSource: MovieRemoteDataSource
    val testScheduler = TestScheduler()
    val mockObserver = mock<Observer<MovieListScreenState>>()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    lateinit var mockRepository: MovieRepository
    lateinit var viewModel: MoviesViewModel
    lateinit var movies: ArrayList<Movie>
    lateinit var moviesCached: ArrayList<Movie>

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)

        mockRepository = MovieRepository(mockNetManager, mockMovieRemoteDataSource, mockMovieLocalDataSource)

        viewModel = spy(MoviesViewModel(mockRepository, testScheduler, testScheduler))

        val movie1 = Movie(1, "", "Mock movie 1", "0", "0", "0", "")
        movies = Lists.newArrayList(movie1)

        val movieCached1 = Movie(1, "", "Mock cached movie 1", "0", "0", "0", "")
        moviesCached = Lists.newArrayList(movieCached1)
    }

    @Test
    fun getMoviesRemoteList_Success() {
        Mockito.`when`(mockNetManager.isConnectedToInternet).thenReturn(true)
        Mockito.`when`(mockMovieRemoteDataSource.getMovies("1", "")).thenReturn(Observable.just(MovieListResponse(1, movies)))

        viewModel.moviesLiveData.observeForever(mockObserver)

        viewModel.listMovies("")
        verify(viewModel).listMovies("")

        assertTrue("Is loading", viewModel.isLoading.get())

        testScheduler.triggerActions()

        assertFalse("Finished loading", viewModel.isLoading.get())

        assertEquals("Invalid Status", ScreenStatus.OK.status, viewModel.moviesLiveData.value?.status)
        assertTrue("No data found", viewModel.moviesLiveData.value?.movies?.size ?: 0 > 0)
        assertEquals("Wrong list received","Mock movie 1", viewModel.moviesLiveData.value?.movies?.get(0)?.title)
    }

    @Test
    fun getMoviesLocalList_Success() {
        Mockito.`when`(mockNetManager.isConnectedToInternet).thenReturn(false)
        Mockito.`when`(mockMovieLocalDataSource.getMovies("1", "")).thenReturn(Observable.just(MovieListResponse(1, moviesCached)))

        viewModel.moviesLiveData.observeForever(mockObserver)

        viewModel.listMovies("")
        verify(viewModel).listMovies("")

        assertTrue("Is loading", viewModel.isLoading.get())

        testScheduler.triggerActions()

        assertFalse("Finished loading", viewModel.isLoading.get())

        assertEquals("Invalid Status", ScreenStatus.OK.status, viewModel.moviesLiveData.value?.status)
        assertTrue("No data found",viewModel.moviesLiveData.value?.movies?.size ?: 0 > 0)
        assertEquals("Wrong list received","Mock cached movie 1", viewModel.moviesLiveData.value?.movies?.get(0)?.title)

    }
}
