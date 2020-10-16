package com.stenleone.stanleysfilm.network.entity.movie

import com.google.gson.annotations.SerializedName
import java.util.ArrayList

data class MoviesEntity(
    val page: Int,
    @SerializedName("results")
    val movies: ArrayList<Movie>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)