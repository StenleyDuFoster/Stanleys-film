package com.stenleone.stanleysfilm.network.entity.images

import com.stenleone.stanleysfilm.interfaces.model.Network

data class ImagesEntity(
    val backdrops: ArrayList<String>,
    val id: Int,
    val posters: ArrayList<PosterUI>
): Network