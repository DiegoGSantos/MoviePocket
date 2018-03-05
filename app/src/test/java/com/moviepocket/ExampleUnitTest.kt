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
    lateinit var movies: List<Movie>
    @Captor private lateinit var loadTasksCallbackCaptor: ArgumentCaptor<MovieRepository.LoadMoviesCallback>

    companion object{
        @BeforeClass
        fun setUpRxSchedulers() {
            val immediate = object : Scheduler() {
                override fun scheduleDirect(run: Runnable, delay: Long, unit: TimeUnit): Disposable {
                    // this prevents StackOverflowErrors when scheduling with a delay
                    return super.scheduleDirect(run, 0, unit)
                }

                override fun createWorker(): Worker {
                    return ExecutorScheduler.ExecutorWorker(Executor { it.run() })
                }
            }

            RxJavaPlugins.setInitIoSchedulerHandler { scheduler -> immediate }
            RxJavaPlugins.setInitComputationSchedulerHandler { scheduler -> immediate }
            RxJavaPlugins.setInitNewThreadSchedulerHandler { scheduler -> immediate }
            RxJavaPlugins.setInitSingleSchedulerHandler { scheduler -> immediate }
            RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler -> immediate }
        }
    }

    @Before
    fun setUpTest() {

        setUpRxSchedulers()

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

        assertTrue(viewModel.moviesLiveData.value?.size ?: 0 > 0)
        assertFalse(viewModel.moviesLiveData.value?.isEmpty() ?: false)

    }
}
