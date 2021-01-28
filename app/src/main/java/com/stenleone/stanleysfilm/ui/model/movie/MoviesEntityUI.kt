package com.stenleone.stanleysfilm.network.entity.movie

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.stenleone.stanleysfilm.interfaces.model.UI
import kotlinx.android.parcel.Parcelize
import java.util.ArrayList

@Parcelize
data class MoviesEntityUI(
    val page: Int,
    @SerializedName("results")
    val movies: ArrayList<MovieUI>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
): Parcelable, UI