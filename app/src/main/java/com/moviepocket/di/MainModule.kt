package com.moviepocket.di

import com.moviepocket.features.movieDetail.model.data.MovieDetailRemoteDataSource
import com.moviepocket.features.movieDetail.model.data.MovieDetailRepository
import com.moviepocket.features.movieDetail.viewmodel.MovieDetailViewModel
import com.moviepocket.features.moviesList.model.data.MovieLocalDataSource
import com.moviepocket.features.moviesList.model.data.MovieRemoteDataSource
import com.moviepocket.features.moviesList.model.data.MovieRepository
import com.moviepocket.features.moviesList.viewmodel.MoviesViewModel
import com.moviepocket.manager.NetManager
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by diego.santos on 26/04/18.
 */
val mainModule = module {
    viewModel { MoviesViewModel(get()) }
    viewModel { MovieDetailViewModel(get()) }

    single { MovieRepository(get(), get()) }
    single { NetManager() }
    single { MovieRemoteDataSource() }
    single { MovieLocalDataSource() }

    single { MovieDetailRepository(get()) }
    single { MovieDetailRemoteDataSource() }
}