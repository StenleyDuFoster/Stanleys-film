package com.stenleone.stanleysfilm.network.entity.movie

import com.google.gson.annotations.SerializedName

data class MoviesEntity(
    val page: Int,
    val movies: List<Movie>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)