package com.stenleone.stanleysfilm.network.entity.movie

import com.google.gson.annotations.SerializedName
import com.stenleone.stanleysfilm.interfaces.model.UI

data class BelongsToCollectionUI(
    @SerializedName("backdrop_path")
    val backdropPath: String,
    val id: Int,
    val name: String,
    @SerializedName("poster_path")
    val posterPath: String
): UI