package com.moviepocket.restclient

import com.moviepocket.restclient.response.GenreListResponse
import com.moviepocket.restclient.response.MovieListResponse
import com.moviepocket.util.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import com.google.gson.GsonBuilder
import retrofit2.converter.gson.GsonConverterFactory


/**
 * Created by diegosantos on 12/16/17.
 */
interface Service {

//    @retrofit2.http.Headers(" : ")
//    @retrofit2.http.POST("transaction/app")
//    fun restCall(@retrofit2.http.Body parameter: Parameter): io.reactivex.Observable<Response<ResponseBody>>

    @retrofit2.http.GET("movie/now_playing?api_key=08be2ddccd76e47b36c5eaa4797bd6a4&language=en-US&page=1/")
    fun listMovies(): io.reactivex.Observable<MovieListResponse>

    @retrofit2.http.GET("genre/movie/list?api_key=08be2ddccd76e47b36c5eaa4797bd6a4&language=en-US")
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