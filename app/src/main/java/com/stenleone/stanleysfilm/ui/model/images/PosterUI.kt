package com.stenleone.stanleysfilm.network.entity.images

import com.stenleone.stanleysfilm.interfaces.model.UI

data class PosterUI(
    val aspect_ratio: Double,
    val file_path: String,
    val height: Int,
    val iso_639_1: String,
    val vote_average: Double,
    val vote_count: Int,
    val width: Int
): UI