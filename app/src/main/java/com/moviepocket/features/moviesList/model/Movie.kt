package com.moviepocket.features.moviesList.model

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import com.activeandroid.Model
import com.activeandroid.annotation.Column
import com.activeandroid.annotation.Table
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.activeandroid.query.Delete
import com.activeandroid.query.Select
import com.moviepocket.util.adapter.AdapterConstants
import com.moviepocket.util.adapter.ViewType

/**
 * Created by diegosantos on 12/16/17.
 */
@SuppressLint("ParcelCreator")
@Table(name = "Movies")
class Movie(@Expose @Column(name = "posterPath")
            @SerializedName("poster_path")
            val posterPath: String = "",
            @Expose @Column(name = "title")
            @SerializedName("title")
            val title: String = "",
            @Expose @Column(name = "voteAverage")
            @SerializedName("vote_average")
            val voteAverage: String = "",
            @Expose @Column(name = "movieId")
            @SerializedName("id")
            val movieId: String = "",
            @Column(name = "listType")
            var listType: String = "") : Model(), ViewType, Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun getViewType() = AdapterConstants.MOVIE

    fun getPosterUrl(): String = "http://image.tmdb.org/t/p/w342$posterPath"

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(posterPath)
        parcel.writeString(title)
        parcel.writeString(voteAverage)
        parcel.writeString(movieId)
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

        fun getAllFromType(listType: String): List<Movie> {
            return Select()
                    .from(Movie::class.java)
                    .where("listType='"+listType+"'")
                    .execute()
        }

        fun getAll(): List<Movie> {
            return Select()
                    .from(Movie::class.java)
                    .execute()
        }

        fun deleteAll(): List<Movie>? {
            return Delete().from(Movie::class.java).execute()
        }

        fun deleteAllFromType(listType: String): List<Movie>? {
            return Delete().from(Movie::class.java).where("listType='"+listType+"'").execute()
        }
    }
}