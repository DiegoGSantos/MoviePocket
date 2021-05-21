package com.moviepocket.features.moviesList.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moviepocket.features.Event
import com.moviepocket.features.moviesList.model.data.MovieRepository
import com.moviepocket.features.moviesList.model.domain.Movie
import com.moviepocket.manager.NetManager
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

/**
 * Created by diego.santos on 01/02/18.
 */
class MoviesViewModel(private val movieRepository: MovieRepository,
                      private val netManager: NetManager = NetManager()): ViewModel() {

    val moviesScreenState: MutableLiveData<MovieListScreenState> = MutableLiveData()
    val movieScreenEvent: MutableLiveData<Event<MovieListScreenEvent>> = MutableLiveData()
    val moviesList = ArrayList<Movie>()
    private var totalOfPages: Int = 0

    fun onViewCreated(listType: String) {
        listMovies(listType, true)
    }

    fun onNewPageRequested(listType: String) {
        listMovies(listType, false)
    }

    fun onMovieClicked(movie: Movie) {
        if (netManager.isConnectedToInternet){
            movieScreenEvent.value = Event(MovieListScreenEvent.OpenMovieDetail(movie))
        } else{
            movieScreenEvent.value = Event(MovieListScreenEvent.Error(Throwable()))
        }
    }

    fun onMovieLongClicked(movie: Movie) {
        if (netManager.isConnectedToInternet){
            movieScreenEvent.value = Event(MovieListScreenEvent.OpenMoviePreview(movie))
        } else{
            movieScreenEvent.value = Event(MovieListScreenEvent.Error(Throwable()))
        }
    }

    private fun listMovies(listType: String, isStartingView: Boolean) {
        val pageToBeLoaded = getPageToLoad(isStartingView)

        if (isStartingView && isRestartingPage(pageToBeLoaded) && stateIsNotOnError()) {
            if (moviesList.size > 0) {
                onMoviesLoaded(
                    (moviesScreenState.value?.currentPage ?: 1),
                    moviesList,
                    totalOfPages
                )
            } else {
                onDataNoAvailable(pageToBeLoaded)
            }
        } else if (isThereMoreItemsToLoad(pageToBeLoaded)) {

            if (pageToBeLoaded == 1) onLoading()

            getFreshListOfMovies(pageToBeLoaded, listType)
        }
    }

    private fun getFreshListOfMovies(pageToBeLoaded: Int, listType: String) {
        viewModelScope.launch {
            movieRepository.getMovies(pageToBeLoaded.toString(), listType)
                .onSuccess { result ->
                    if (result.results.size > 0) {
                        onMoviesLoaded(pageToBeLoaded, result.results, result.totalPages)
                        moviesList.addAll(result.results)
                        movieRepository.saveMovies(
                            result.results,
                            listType,
                            pageToBeLoaded.toString()
                        )
                    } else {
                        onDataNoAvailable(pageToBeLoaded)
                    }
                }
                .onFailure {
                    onRequestError(pageToBeLoaded)
                    it.printStackTrace()
                }
        }
    }

    private fun stateIsNotOnError() = !(moviesScreenState.value?.isThereError() ?: false)

    private fun getPageToLoad(isStartingView: Boolean): Int {
        var pageToBeLoaded = 1
        if (!isStartingView) {
            pageToBeLoaded = 1 + (moviesScreenState.value?.currentPage ?: 0)
        }
        return pageToBeLoaded
    }

    private fun isRestartingPage(pageToBeLoaded: Int): Boolean {
        var isRestartingPage = false
        moviesScreenState.value?.let {
            isRestartingPage = pageToBeLoaded <= it.currentPage
                    && moviesList.size >= it.movies.size
        }
        return isRestartingPage
    }

    private fun isThereMoreItemsToLoad(pageToBeLoaded: Int): Boolean {
        return totalOfPages == 0 || pageToBeLoaded <= totalOfPages
    }

    private fun onMoviesLoaded(pageLoaded: Int, movies: List<Movie>, totalPages: Int) {
        totalOfPages = totalPages
        val nextPage = pageLoaded + 1
        moviesScreenState.value = MovieListScreenState(pageLoaded, isThereMoreItemsToLoad(nextPage), ScreenStatus.OK.status, "", movies)
    }

    private fun onDataNoAvailable(pageLoaded: Int) {
        moviesScreenState.value = MovieListScreenState(pageLoaded, false, ScreenStatus.NO_DATA_FOUND.status,  "", emptyList())
    }

    private fun onRequestError(pageLoaded: Int) {
        moviesScreenState.value = MovieListScreenState(pageLoaded, true, ScreenStatus.ERROR.status,  "", emptyList())
    }

    private fun onLoading() {
        moviesScreenState.value = MovieListScreenState(1, true, ScreenStatus.LOADING.status,  "", emptyList())
    }
}
