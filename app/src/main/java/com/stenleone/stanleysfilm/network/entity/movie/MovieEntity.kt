package com.stenleone.stanleysfilm.network.entity.movie

data class MovieEntity(
    val page: Int,
    val results: List<Result>,
    val total_pages: Int,
    val total_results: Int
)