package com.stenleone.stanleysfilm.network.entity.images

data class ImagesEntity(
    val backdrops: ArrayList<String>,
    val id: Int,
    val posters: ArrayList<Poster>
)