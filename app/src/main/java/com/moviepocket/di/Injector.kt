package com.moviepocket.di

import android.content.Context
import com.moviepocket.features.movieDetail.data.MovieDetailRemoteDataSource
import com.moviepocket.features.movieDetail.data.MovieDetailRepository
import com.moviepocket.features.movieDetail.viewmodel.MovieDetailViewModelFactory
import com.moviepocket.features.moviesList.data.MovieLocalDataSource
import com.moviepocket.features.moviesList.data.MovieRemoteDataSource
import com.moviepocket.features.moviesList.data.MovieRepository
import com.moviepocket.features.moviesList.viewmodel.MoviesViewModelFactory
import com.moviepocket.manager.NetManager
import com.moviepocket.restclient.Service
import io.reactivex.Scheduler

/**
 * Created by diego.santos on 27/02/18.
 */
object Injector {
    fun provideNetManager(context: Context): NetManager {
        return NetManager(context)
    }

    private fun provideMovieRepository(netManager: NetManager,
                                       movieRemoteDataSource: MovieRemoteDataSource,
                                       movieLocalDataSource: MovieLocalDataSource): MovieRepository {
        return MovieRepository(netManager, movieRemoteDataSource, movieLocalDataSource)
    }

    private fun provideMovieDetailRepository(movieDetailRemoveDataSource: MovieDetailRemoteDataSource): MovieDetailRepository {
        return MovieDetailRepository(movieDetailRemoveDataSource)
    }

    fun provideMoviesViewModelFactory(context: Context, processScheduler: Scheduler,
                                      androidScheduler: Scheduler): MoviesViewModelFactory {
        val repository = provideMovieRepository(provideNetManager(context), provideMovieRemoteDataSource(), provideMovieLocalDataSource())
        return MoviesViewModelFactory(repository, processScheduler, androidScheduler)
    }

    fun provideMovieDetailViewModelFactory(processScheduler: Scheduler,
                                           androidScheduler: Scheduler): MovieDetailViewModelFactory {
        val movieDetailDataSource = provideMovieDetailRemoteDataSource()
        val repository = provideMovieDetailRepository(movieDetailDataSource)
        return MovieDetailViewModelFactory(repository, processScheduler, androidScheduler)
    }

    fun provideService(): Service {
        return Service.Factory.create()
    }

    fun provideMovieRemoteDataSource(): MovieRemoteDataSource {
        return MovieRemoteDataSource(provideService())
    }

    fun provideMovieLocalDataSource(): MovieLocalDataSource {
        return MovieLocalDataSource()
    }

    fun provideMovieDetailRemoteDataSource(): MovieDetailRemoteDataSource {
        return MovieDetailRemoteDataSource(provideService())
    }
}