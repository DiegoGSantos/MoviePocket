package com.moviepocket.di

import android.content.Context
import com.moviepocket.features.movieDetail.viewmodel.MovieDetailViewModelFactory
import com.moviepocket.features.moviesList.data.MovieRepository
import com.moviepocket.features.moviesList.viewmodel.MoviesViewModelFactory
import com.moviepocket.manager.NetManager

/**
 * Created by diego.santos on 27/02/18.
 */
object Injector {
    fun provideNetManager(context: Context): NetManager {
        return NetManager(context)
    }

    private fun provideMovieRepository(netManager: NetManager): MovieRepository {
        return MovieRepository(netManager)
    }

    fun provideMoviesViewModelFactory(context: Context): MoviesViewModelFactory {
        val netManager = provideNetManager(context)
        val repository = provideMovieRepository(netManager)
        return MoviesViewModelFactory(repository)
    }

    fun provideMovieDetailViewModelFactory(context: Context): MovieDetailViewModelFactory {
        val netManager = provideNetManager(context)
        val repository = provideMovieRepository(netManager)
        return MovieDetailViewModelFactory(repository)
    }
}