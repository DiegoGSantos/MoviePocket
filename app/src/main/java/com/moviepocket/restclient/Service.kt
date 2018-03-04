package com.moviepocket.restclient

import com.moviepocket.restclient.response.GenreListResponse
import com.moviepocket.restclient.response.MovieListResponse
import com.moviepocket.util.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import com.google.gson.GsonBuilder
import com.moviepocket.restclient.response.MovieDetailResponse
import retrofit2.converter.gson.GsonConverterFactory


/**
 * Created by diegosantos on 12/16/17.
 */
interface Service {

    @retrofit2.http.GET("movie/{listType}?api_key=08be2ddccd76e47b36c5eaa4797bd6a4&language=pt-BR&&region=br")
    fun listMovies(@retrofit2.http.Path("listType") listType: String, @retrofit2.http.Query("page") page: String) : io.reactivex.Observable<MovieListResponse>

    @retrofit2.http.GET("movie/{movieId}?api_key=08be2ddccd76e47b36c5eaa4797bd6a4&language=pt-BR&append_to_response=release_dates,videos")
    fun getMovieDetail(@retrofit2.http.Path("movieId") movieId: String): io.reactivex.Observable<MovieDetailResponse>

    @retrofit2.http.GET("genre/movie/list?api_key=08be2ddccd76e47b36c5eaa4797bd6a4&language=pt-BR")
    fun listGenres(): io.reactivex.Observable<GenreListResponse>

    companion object Factory {
        fun create(): Service {

            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)

            val httpClient = OkHttpClient.Builder()

            httpClient.addInterceptor(logging)

            val retrofit = retrofit2.Retrofit.Builder()
                    .addCallAdapterFactory(retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()))
                    .client(httpClient.build())
                    .baseUrl(Constants.BASE_URL)
                    .build()

            return retrofit.create(Service::class.java);
        }
    }
}