package com.stenleone.stanleysfilm.network.entity

import com.stenleone.stanleysfilm.interfaces.model.Network

data class VideosResult(
    val id: String?,
    val iso_3166_1: String?,
    val iso_639_1: String?,
    val key: String?,
    val name: String?,
    val site: String?,
    val size: String?,
    val type: String?
): Network