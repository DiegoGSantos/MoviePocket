package com.moviepocket.restclient.response

import com.google.gson.annotations.Expose
import com.moviepocket.model.Movie

/**
 * Created by diegosantos on 12/17/17.
 */
class MovieListResponse(@Expose val results: ArrayList<Movie>)