package com.moviepocket.features.moviesList.model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

/**
 * Created by diegosantos on 3/2/18.
 */
@Entity
data class User(
        @Id var id: Long = 0,
        var firstName: String,
        var lastName: String
)