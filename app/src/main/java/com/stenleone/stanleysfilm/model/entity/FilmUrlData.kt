package com.stenleone.stanleysfilm.model.entity

import com.stenleone.stanleysfilm.managers.filmControllers.FilmControllerEnum

data class FilmUrlData(
    var url_360: String? = null,
    var url_480: String? = null,
    var url_720: String? = null,
    var url_1080: String? = null,
    var url_2060: String? = null,
    val provider: FilmControllerEnum
) {
    fun isUrlDataNullOrEmpty() =
        url_360.isNullOrEmpty() && url_480.isNullOrEmpty() && url_720.isNullOrEmpty() && url_1080.isNullOrEmpty() && url_2060.isNullOrEmpty()

    fun getNonNullUrlOrNull(): String? {
        return url_720 ?: url_480 ?: url_360 ?: url_1080 ?: url_2060
    }
}