package com.stenleone.stanleysfilm.network.entity.movie

import com.google.gson.annotations.SerializedName
import com.stenleone.stanleysfilm.interfaces.model.Network

data class BelongsToCollection(
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    val id: String?,
    val name: String?,
    @SerializedName("poster_path")
    val posterPath: String?
): Network