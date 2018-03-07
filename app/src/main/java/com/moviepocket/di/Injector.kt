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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

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

    fun provideMoviesViewModelFactory(context: Context): MoviesViewModelFactory {
        val repository = provideMovieRepository(provideNetManager(context), provideMovieRemoteDataSource(), provideMovieLocalDataSource())
        return MoviesViewModelFactory(repository)
    }

    fun provideMovieDetailViewModelFactory(movieDetailDataSource: MovieDetailRemoteDataSource): MovieDetailViewModelFactory {
        val repository = provideMovieDetailRepository(movieDetailDataSource)
        return MovieDetailViewModelFactory(repository)
    }

    fun provideService(): Service {
        return Service.Factory.create()
    }

    fun provideMovieRemoteDataSource(): MovieRemoteDataSource {
        return MovieRemoteDataSource(provideService(), Schedulers.io(), AndroidSchedulers.mainThread())
    }

    fun provideMovieLocalDataSource(): MovieLocalDataSource {
        return MovieLocalDataSource(Schedulers.io(), AndroidSchedulers.mainThread())
    }

    fun provideMovieDetailRemoteDataSource(): MovieDetailRemoteDataSource {
        return MovieDetailRemoteDataSource(provideService(), Schedulers.io(), AndroidSchedulers.mainThread())
    }
}