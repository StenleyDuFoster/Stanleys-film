package com.stenleone.stanleysfilm.network.entity.movie

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.stenleone.stanleysfilm.interfaces.model.Network
import kotlinx.android.parcel.Parcelize
import java.util.ArrayList

data class MoviesEntity(
    val page: String?,
    @SerializedName("results")
    val movies: ArrayList<Movie>?,
    @SerializedName("total_pages")
    val totalPages: String?,
    @SerializedName("total_results")
    val totalResults: String?
): Network