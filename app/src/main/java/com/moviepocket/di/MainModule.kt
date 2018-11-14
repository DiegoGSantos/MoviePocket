package com.moviepocket.di

import com.moviepocket.features.movieDetail.data.MovieDetailRemoteDataSource
import com.moviepocket.features.movieDetail.data.MovieDetailRepository
import com.moviepocket.features.movieDetail.viewmodel.MovieDetailViewModel
import com.moviepocket.features.movieDetail.viewmodel.MovieDetailViewModelFactory
import com.moviepocket.features.moviesList.data.MovieLocalDataSource
import com.moviepocket.features.moviesList.data.MovieRemoteDataSource
import com.moviepocket.features.moviesList.data.MovieRepository
import com.moviepocket.features.moviesList.viewmodel.MoviesViewModel
import com.moviepocket.features.moviesList.viewmodel.MoviesViewModelFactory
import com.moviepocket.manager.NetManager
import org.koin.android.architecture.ext.viewModel
import org.koin.dsl.module.Module
import org.koin.dsl.module.applicationContext

/**
 * Created by diego.santos on 26/04/18.
 */
val module : Module = applicationContext {
    factory { MoviesViewModelFactory(get()) }
    bean { MovieRepository(get(), get(), get()) }
    bean { NetManager() }
    bean { MovieRemoteDataSource() }
    bean { MovieLocalDataSource() }

    factory { MovieDetailViewModelFactory(get()) }
    bean { MovieDetailRepository(get()) }
    bean { MovieDetailRemoteDataSource() }

    viewModel { MoviesViewModel(get()) }
    viewModel { MovieDetailViewModel(get()) }
}