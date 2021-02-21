package com.stenleone.stanleysfilm.network.entity.movie

import com.stenleone.stanleysfilm.interfaces.model.Network

data class ProductionCountry(
    val iso_3166_1: String?,
    val name: String?
): Network