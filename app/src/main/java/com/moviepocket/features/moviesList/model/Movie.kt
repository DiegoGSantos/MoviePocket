package com.moviepocket.features.moviesList.model

import com.activeandroid.Model
import com.activeandroid.annotation.Column
import com.activeandroid.annotation.Table
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.activeandroid.query.Delete
import com.activeandroid.query.Select
/**
 * Created by diegosantos on 12/16/17.
 */
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
            val movieId: String = "") : Model(){

    companion object {
        val ID = "movie_id_extra"

        fun getAll(): List<Movie> {
            return Select()
                    .from(Movie::class.java)
                    .execute()
        }

        fun deleteAll(): List<Movie>? {
            return Delete().from(Movie::class.java).execute()
        }
    }

    fun getPosterUrl(): String = "http://image.tmdb.org/t/p/w342$posterPath"
}