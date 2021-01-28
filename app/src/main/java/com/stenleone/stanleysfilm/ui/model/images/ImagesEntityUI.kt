package com.stenleone.stanleysfilm.network.entity.images

import com.stenleone.stanleysfilm.interfaces.model.UI

data class ImagesEntityUI(
    val backdrops: ArrayList<String>,
    val id: Int,
    val posters: ArrayList<PosterUI>
): UI