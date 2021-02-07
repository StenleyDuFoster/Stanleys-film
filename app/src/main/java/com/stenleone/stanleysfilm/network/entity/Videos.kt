package com.stenleone.stanleysfilm.network.entity

import com.stenleone.stanleysfilm.interfaces.model.Network

data class Videos(
    val id: String?,
    val results: ArrayList<VideosResult>?
): Network