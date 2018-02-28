package com.moviepocket.features.moviesList.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.databinding.ObservableField
import com.moviepocket.features.moviesList.data.MovieListTypes
import com.moviepocket.features.moviesList.data.MovieRepository
import com.moviepocket.features.moviesList.model.Movie
import com.moviepocket.restclient.Service

/**
 * Created by diego.santos on 01/02/18.
 */
class MoviesViewModel(val movieRepository: MovieRepository): ViewModel() {

    var moviesLiveData: MutableLiveData<List<Movie>> = MutableLiveData()
    val isLoading = ObservableField(false)

    var totalOfPages: Int = 0

    var isThereMoreItemsToLoad: Boolean = true
    var currentPage: Int = 1

    var currentInTheaterPage: Int = 1
    var currentUpcomingPage: Int = 1
    var currentPopularPage: Int = 1
    var currentTopRatedPage: Int = 1

    var isThereMoreInTheaterToLoad: Boolean = true
    var isThereMoreUpcomingToLoad: Boolean = true
    var isThereMorePopularToLoad: Boolean = true
    var isThereMoreTopRatedToLoad: Boolean = true

    fun listMovies(listType: String) {

        isLoading.set(true)

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
            movieRepository.getMovies(currentPage, listType) { error, movies, totalPages ->
                totalOfPages = totalPages

                if (listType.equals(MovieListTypes.NOW_PLAYING.listType)) {
                    currentInTheaterPage += 1
                    isThereMoreInTheaterToLoad = currentInTheaterPage <= totalOfPages
                } else if (listType.equals(MovieListTypes.UPCOMING.listType)) {
                    currentUpcomingPage += 1
                    isThereMoreUpcomingToLoad = currentUpcomingPage <= totalOfPages
                }else if (listType.equals(MovieListTypes.POPULAR.listType)) {
                    currentPopularPage += 1
                    isThereMorePopularToLoad = currentPopularPage <= totalOfPages
                }else if (listType.equals(MovieListTypes.TOP_RATED.listType)) {
                    currentTopRatedPage += 1
                    isThereMoreTopRatedToLoad = currentTopRatedPage <= totalOfPages
                }

                moviesLiveData.value = movies
                isLoading.set(false)
            }
        }
    }

    fun isThereMoreItemsToLoad(listType: String): Boolean {
        if (listType.equals(MovieListTypes.NOW_PLAYING.listType)) return isThereMoreInTheaterToLoad
        else if (listType.equals(MovieListTypes.UPCOMING.listType)) return isThereMoreUpcomingToLoad
        else if (listType.equals(MovieListTypes.POPULAR.listType)) return isThereMorePopularToLoad
        else if (listType.equals(MovieListTypes.TOP_RATED.listType)) return isThereMoreTopRatedToLoad
        else return false
    }

    fun getCurrentPage(listType: String): Int {
        if (listType.equals(MovieListTypes.NOW_PLAYING.listType)) return currentInTheaterPage
        else if (listType.equals(MovieListTypes.UPCOMING.listType)) return currentUpcomingPage
        else if (listType.equals(MovieListTypes.POPULAR.listType)) return currentPopularPage
        else if (listType.equals(MovieListTypes.TOP_RATED.listType)) return currentTopRatedPage
        else return 1
    }
}
