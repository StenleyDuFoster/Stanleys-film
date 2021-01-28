package com.stenleone.stanleysfilm.network.entity.movie

import com.google.gson.annotations.SerializedName
import com.stenleone.stanleysfilm.interfaces.model.UI

data class SpokenLanguageUI(
    @SerializedName("english_name")
    val englishName: String,
    val iso_639_1: String,
    val name: String
): UI