package com.stenleone.stanleysfilm.network.entity.movie

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.stenleone.stanleysfilm.interfaces.model.Network
import kotlinx.android.parcel.Parcelize

data class ProductionCompany(
    val id: String?,
    @SerializedName("logo_path")
    val logoPath: String?,
    val name: String?,
    @SerializedName("origin_country")
    val originCountry: String?
): Network