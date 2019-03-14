package com.moviepocket.di

import com.moviepocket.features.movieDetail.data.MovieDetailRemoteDataSource
import com.moviepocket.features.movieDetail.data.MovieDetailRepository
import com.moviepocket.features.movieDetail.viewmodel.MovieDetailViewModel
import com.moviepocket.features.moviesList.data.MovieLocalDataSource
import com.moviepocket.features.moviesList.data.MovieRemoteDataSource
import com.moviepocket.features.moviesList.data.MovieRepository
import com.moviepocket.features.moviesList.viewmodel.MoviesViewModel
import com.moviepocket.manager.NetManager
import org.koin.android.architecture.ext.viewModel
import org.koin.dsl.module.Module
import org.koin.dsl.module.applicationContext

/**
 * Created by diego.santos on 26/04/18.
 */
val module : Module = applicationContext {
    bean { MovieRepository(get(), get(), get()) }
    bean { NetManager() }
    bean { MovieRemoteDataSource() }
    bean { MovieLocalDataSource() }

    bean { MovieDetailRepository(get()) }
    bean { MovieDetailRemoteDataSource() }

    viewModel { MoviesViewModel(get()) }
    viewModel { MovieDetailViewModel(get()) }
}