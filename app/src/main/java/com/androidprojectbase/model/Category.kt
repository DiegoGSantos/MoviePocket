package com.androidprojectbase.model

import com.activeandroid.Model
import com.activeandroid.annotation.Column
import com.activeandroid.annotation.Table
import com.activeandroid.query.Select
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.moviepocket.model.Movie

/**
 * Created by diegosantos on 2/4/18.
 */
@Table(name = "Categories")
class Category(@Expose @Column(name = "posterPath")
               @SerializedName("poster_path")
               val posterPath: String = "",
               @Expose @Column(name = "originalTitle")
               @SerializedName("original_title")
               val originalTitle: String = "",
               @Expose @Column(name = "voteAverage")
               @SerializedName("vote_average")
               val voteAverage: String = "") : Model() {

    companion object {
        fun getAll(): List<Category> {
            return Select()
                    .from(Category::class.java)
                    .execute()
        }
    }
}