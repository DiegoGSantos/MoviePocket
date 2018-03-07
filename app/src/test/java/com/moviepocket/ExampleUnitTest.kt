package com.moviepocket

import com.google.common.collect.Lists
import com.moviepocket.features.moviesList.data.MovieRepository
import com.moviepocket.features.moviesList.model.Movie
import com.moviepocket.features.moviesList.viewmodel.MoviesViewModel
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.mockito.*
import org.mockito.Mockito.verify
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.internal.schedulers.ExecutorScheduler
import android.support.annotation.NonNull
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.junit.BeforeClass
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Mock lateinit var mockRepository: MovieRepository
    lateinit var viewModel: MoviesViewModel
    lateinit var movies: ArrayList<Movie>
    @Captor private lateinit var loadTasksCallbackCaptor: ArgumentCaptor<MovieRepository.LoadMoviesCallback>

    @Before
    fun setUpTest() {
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }

        MockitoAnnotations.initMocks(this)
        viewModel = MoviesViewModel(mockRepository)

        val movie1 = Movie(1, "", "Mock movie 1", "0", "0", "0", "")

        movies = Lists.newArrayList(movie1)
    }

    @Test
    fun getMoviesList_Success() {
//        Mockito.`when`(mockRepository.getMovies("1", "", { error, movies, totalPages ->})).thenAnswer {
//            val argument = it.arguments[2]
//            val completion = argument as ((error: Any?, movies: List<Movie>, totalPages: String) -> Unit)
//            completion.invoke(null, movies, "1")
//        }

        viewModel.listMovies("")

        verify<MovieRepository>(mockRepository).getMovies(eq("1"), eq(""), capture(loadTasksCallbackCaptor))
        loadTasksCallbackCaptor.value.onMoviesLoaded(null, movies, "1")

//        assertTrue(viewModel.moviesLiveData.value?.size ?: 0 > 0)
//        assertFalse(viewModel.moviesLiveData.value?.isEmpty() ?: false)

    }
}
