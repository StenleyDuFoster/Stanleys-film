package com.stenleone.stanleysfilm.network.entity.movie

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.stenleone.stanleysfilm.interfaces.model.UI
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductionCompanyUI(
    val id: Int,
    @SerializedName("logo_path")
    val logoPath: String,
    val name: String,
    @SerializedName("origin_country")
    val originCountry: String
): Parcelable, UI