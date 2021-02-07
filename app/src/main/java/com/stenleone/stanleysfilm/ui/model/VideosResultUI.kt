package com.stenleone.stanleysfilm.ui.model

import android.os.Parcelable
import com.stenleone.stanleysfilm.interfaces.model.UI
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VideosResultUI(
    val id: String,
    val iso_3166_1: String,
    val iso_639_1: String,
    val key: String,
    val name: String,
    val site: String,
    val size: Int,
    val type: String
): UI, Parcelable