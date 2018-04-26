package com.moviepocket.features.moviesList.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import com.moviepocket.App
import com.moviepocket.R
import com.moviepocket.features.moviesList.data.MovieListTypes
import com.moviepocket.features.moviesList.data.MovieRepository
import com.moviepocket.features.moviesList.model.Movie
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable


/**
 * Created by diego.santos on 01/02/18.
 */
class MoviesViewModel(private val movieRepository: MovieRepository,
                      private val processScheduler: Scheduler,
                      private val androidScheduler: Scheduler): ViewModel() {

    var moviesLiveData: MutableLiveData<MovieListScreenState> = MutableLiveData()
    val isLoading = ObservableField(false)
    val hasError = ObservableField(false)
    val errorMessage = ObservableField("")

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

        if (listType.equals(MovieListTypes.NOW_PLAYING.listType)) {
            currentPage = currentInTheaterPage
            isThereMoreItemsToLoad = isThereMoreInTheaterToLoad
        } else if (listType.equals(MovieListTypes.UPCOMING.listType)) {
            currentPage = currentUpcomingPage
            isThereMoreItemsToLoad = isThereMoreUpcomingToLoad
        }else if (listType.equals(MovieListTypes.POPULAR.listType)) {
            currentPage = currentPopularPage
            isThereMoreItemsToLoad = isThereMorePopularToLoad
        }else if (listType.equals(MovieListTypes.TOP_RATED.listType)) {
            currentPage = currentTopRatedPage
            isThereMoreItemsToLoad = isThereMoreTopRatedToLoad
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
//                        onRequestError()
                        onDataNotAvailable()
                        error.printStackTrace()
                    })

            compositeDisposable.add(disposable)
        }
    }

    fun isThereMoreItemsToLoad(listType: String): Boolean {
        return when {
            listType.equals(MovieListTypes.NOW_PLAYING.listType) -> isThereMoreInTheaterToLoad
            listType.equals(MovieListTypes.UPCOMING.listType) -> isThereMoreUpcomingToLoad
            listType.equals(MovieListTypes.POPULAR.listType) -> isThereMorePopularToLoad
            listType.equals(MovieListTypes.TOP_RATED.listType) -> isThereMoreTopRatedToLoad
            else -> false
        }
    }

    fun getCurrentPage(listType: String): Int {
        return when {
            listType.equals(MovieListTypes.NOW_PLAYING.listType) -> currentInTheaterPage
            listType.equals(MovieListTypes.UPCOMING.listType) -> currentUpcomingPage
            listType.equals(MovieListTypes.POPULAR.listType) -> currentPopularPage
            listType.equals(MovieListTypes.TOP_RATED.listType) -> currentTopRatedPage
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

        moviesLiveData.value = MovieListScreenState(ScreenStatus.OK.status, "", movies)
        errorMessage.set("")
        isLoading.set(false)
        hasError.set(false)
    }

    private fun onDataNotAvailable() {
        moviesLiveData.value = MovieListScreenState(ScreenStatus.NO_DATA_FOUND.status,  "", emptyList())
        errorMessage.set(App.appContext.getString(R.string.movie_list_error))
        hasError.set(true)
        isLoading.set(false)
    }

    private fun onRequestError() {
        moviesLiveData.value = MovieListScreenState(ScreenStatus.ERROR.status,  "", emptyList())
        errorMessage.set(App.appContext.getString(R.string.app_name))
        hasError.set(true)
        isLoading.set(false)
    }

    private fun unSubscribeFromObservable() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }

    private fun resetState() {
        if (currentPage == 1) {
            isLoading.set(true)
        }
        hasError.set(false)
        errorMessage.set("")
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
