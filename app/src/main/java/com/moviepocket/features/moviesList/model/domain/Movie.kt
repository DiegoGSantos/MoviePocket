package com.moviepocket.features.moviesList.model.domain

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.moviepocket.App
import com.moviepocket.restclient.response.MovieListResponse
import com.moviepocket.util.adapter.AdapterConstants
import com.moviepocket.util.adapter.ViewType
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

/**
 * Created by diegosantos on 12/16/17.
 */
@SuppressLint("ParcelCreator")
@Entity
class Movie(@Id var id: Long = 0,
            @Expose
            @SerializedName("poster_path")
            val posterPath: String? = "",
            @Expose
            @SerializedName("title")
            val title: String = "",
            @Expose
            @SerializedName("vote_average")
            val voteAverage: String = "",
            @Expose
            @SerializedName("id")
            val movieId: String = "",
            var page: String = "",
            var listType: String = "") : ViewType, Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun getViewType() = AdapterConstants.MOVIE

    fun getPosterUrl(): String = "http://image.tmdb.org/t/p/w342$posterPath"

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(posterPath ?: "")
        parcel.writeString(title)
        parcel.writeString(voteAverage)
        parcel.writeString(movieId)
        parcel.writeString(page)
        parcel.writeString(listType)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Movie> {
        override fun createFromParcel(parcel: Parcel): Movie {
            return Movie(parcel)
        }

        override fun newArray(size: Int): Array<Movie?> {
            return arrayOfNulls(size)
        }

        val MOVIE = "movie_extra"

        fun getAllFromType(listType: String, page: String): MovieListResponse {

            val totalOfPages = getNumberOfPages(listType)
            val result: ArrayList<Movie> = ArrayList(App.getMovieBox().query()
                    .equal(Movie_.listType, listType)
                    .equal(Movie_.page, page)
                    .build().find())

            MovieListResponse(totalOfPages, result)

            return MovieListResponse(totalOfPages, result)


        }

        fun getNumberOfPages(listType: String): Int {
            return App.getMovieBox().query()
                    .equal(Movie_.listType, listType)
                    .build()
                    .property(Movie_.page).distinct().findStrings().size
        }

        fun deleteAllFromType(listType: String, page: String) {
            App.getMovieBox().query()
                    .equal(Movie_.listType, listType)
                    .equal(Movie_.page, page)
                    .build().remove()
        }
    }
}