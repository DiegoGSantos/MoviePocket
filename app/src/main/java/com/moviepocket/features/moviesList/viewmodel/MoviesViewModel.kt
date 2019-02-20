package com.moviepocket.features.moviesList.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
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

    var currentPage: Int = 1
    var isThereMoreToLoad: Boolean = true

    private var compositeDisposable = CompositeDisposable()

    fun listMovies(listType: String) {
        if (isThereMoreToLoad) {

            resetState()

            val disposable = movieRepository.getMovies(currentPage.toString(), listType)
                    .observeOn(androidScheduler)
                    .subscribeOn(processScheduler)
                    .subscribe ({
                        result ->
                        if (result.results.size > 0) {
                            onMoviesLoaded(result.results, result.totalPages.toString())
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

    fun isThereMoreItemsToLoad(): Boolean {
        return isThereMoreToLoad
    }

    private fun onMoviesLoaded(movies: List<Movie>, totalPages: String) {
        totalOfPages = totalPages.toIntOrNull() ?: 1
        currentPage += 1
        isThereMoreToLoad = currentPage <= totalOfPages

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
