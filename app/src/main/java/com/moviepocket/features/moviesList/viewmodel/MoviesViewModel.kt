package com.moviepocket.features.moviesList.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.moviepocket.features.moviesList.data.MovieListTypes
import com.moviepocket.features.moviesList.data.MovieRepository
import com.moviepocket.features.moviesList.model.Movie
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


/**
 * Created by diego.santos on 01/02/18.
 */
class MoviesViewModel(private val movieRepository: MovieRepository,
                      private val processScheduler: Scheduler = Schedulers.io(),
                      private val androidScheduler: Scheduler = AndroidSchedulers.mainThread()): ViewModel() {

    var moviesScreenState: MutableLiveData<MovieListScreenState> = MutableLiveData()

    private var totalOfPages: Int = 0

    private var isThereMoreItemsToLoad: Boolean = true
    private var currentPage: Int = 1

    var currentInTheaterPage: Int = 1
    var currentUpcomingPage: Int = 1
    var currentPopularPage: Int = 1
    var currentTopRatedPage: Int = 1

    var isThereMoreInTheaterToLoad: Boolean = true
    var isThereMoreUpcomingToLoad: Boolean = true
    var isThereMorePopularToLoad: Boolean = true
    var isThereMoreTopRatedToLoad: Boolean = true

    private var compositeDisposable = CompositeDisposable()

    fun listMovies(listType: String) {
        when {
            listType.equals(MovieListTypes.NOW_PLAYING.listType) -> {
                currentPage = currentInTheaterPage
                isThereMoreItemsToLoad = isThereMoreInTheaterToLoad
            }
            listType.equals(MovieListTypes.UPCOMING.listType) -> {
                currentPage = currentUpcomingPage
                isThereMoreItemsToLoad = isThereMoreUpcomingToLoad
            }
            listType.equals(MovieListTypes.POPULAR.listType) -> {
                currentPage = currentPopularPage
                isThereMoreItemsToLoad = isThereMorePopularToLoad
            }
            listType.equals(MovieListTypes.TOP_RATED.listType) -> {
                currentPage = currentTopRatedPage
                isThereMoreItemsToLoad = isThereMoreTopRatedToLoad
            }
        }

        if (isThereMoreItemsToLoad) {

            resetState()

            val disposable = movieRepository.getMovies(currentPage.toString(), listType)
                    ?.observeOn(androidScheduler)
                    ?.subscribeOn(processScheduler)
                    ?.subscribe ({
                        result ->
                        if (result.results.size > 0) {
                            onMoviesLoaded(listType, result.results, result.totalPages.toString())
                        } else {
                            onDataNotAvailable()
                        }

                        movieRepository.saveMovies(result.results, listType, currentPage.toString())
                    }, { error ->
                        onRequestError()
                        error.printStackTrace()
                    })

            compositeDisposable.add(disposable)
        }
    }

    fun isThereMoreItemsToLoad(listType: String): Boolean {
        return when {
            listType == MovieListTypes.NOW_PLAYING.listType -> isThereMoreInTheaterToLoad
            listType == MovieListTypes.UPCOMING.listType -> isThereMoreUpcomingToLoad
            listType == MovieListTypes.POPULAR.listType -> isThereMorePopularToLoad
            listType == MovieListTypes.TOP_RATED.listType -> isThereMoreTopRatedToLoad
            else -> false
        }
    }

    fun getCurrentPage(listType: String): Int {
        return when (listType) {
            MovieListTypes.NOW_PLAYING.listType -> currentInTheaterPage
            MovieListTypes.UPCOMING.listType -> currentUpcomingPage
            MovieListTypes.POPULAR.listType -> currentPopularPage
            MovieListTypes.TOP_RATED.listType -> currentTopRatedPage
            else -> 1
        }
    }

    private fun onMoviesLoaded(listType: String, movies: List<Movie>, totalPages: String) {
        totalOfPages = totalPages.toIntOrNull() ?: 1

        when {
            listType.equals(MovieListTypes.NOW_PLAYING.listType) -> {
                currentInTheaterPage += 1
                isThereMoreInTheaterToLoad = currentInTheaterPage <= totalOfPages
            }
            listType.equals(MovieListTypes.UPCOMING.listType) -> {
                currentUpcomingPage += 1
                isThereMoreUpcomingToLoad = currentUpcomingPage <= totalOfPages
            }
            listType.equals(MovieListTypes.POPULAR.listType) -> {
                currentPopularPage += 1
                isThereMorePopularToLoad = currentPopularPage <= totalOfPages
            }
            listType.equals(MovieListTypes.TOP_RATED.listType) -> {
                currentTopRatedPage += 1
                isThereMoreTopRatedToLoad = currentTopRatedPage <= totalOfPages
            }
        }

        moviesScreenState.value = MovieListScreenState(ScreenStatus.OK.status, "", movies)
    }

    private fun onDataNotAvailable() {
        moviesScreenState.value = MovieListScreenState(ScreenStatus.NO_DATA_FOUND.status,  "", emptyList())
    }

    private fun onRequestError() {
        moviesScreenState.value = MovieListScreenState(ScreenStatus.ERROR.status,  "", emptyList())
    }

    private fun onLoading() {
        moviesScreenState.value = MovieListScreenState(ScreenStatus.LOADING.status,  "", emptyList())
    }

    private fun unSubscribeFromObservable() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }

    private fun resetState() {
        if (currentPage == 1) {
            onLoading()
        }
    }

    private fun reset() {
        unSubscribeFromObservable()
        compositeDisposable.clear()
    }

    override fun onCleared() {
        reset()
        super.onCleared()
    }
}
