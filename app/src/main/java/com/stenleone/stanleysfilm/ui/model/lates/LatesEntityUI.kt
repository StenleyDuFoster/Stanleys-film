package com.stenleone.stanleysfilm.network.entity.lates

import com.google.gson.annotations.SerializedName
import com.stenleone.stanleysfilm.interfaces.model.UI
import com.stenleone.stanleysfilm.network.entity.movie.GenreUI
import com.stenleone.stanleysfilm.network.entity.movie.ProductionCompanyUI
import com.stenleone.stanleysfilm.network.entity.movie.ProductionCountryUI

data class LatesEntityUI(
    val adult: Boolean,
    @SerializedName("backdrop_path")
    val backdropPath: Any,
    @SerializedName("belongs_to_collection")
    val belongsToCollection: Any,
    val budget: Int,
    val genres: List<GenreUI>,
    val homepage: String,
    val id: Int,
    @SerializedName("imdb_id")
    val imdbId: String,
    @SerializedName("original_language")
    val originalLanguage: String,
    @SerializedName("original_title")
    val originalTitle: String,
    val overview: String,
    val popularity: Int,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("production_companies")
    val productionCompaniesUI: List<ProductionCompanyUI>,
    @SerializedName("production_countries")
    val productionCountriesUI: List<ProductionCountryUI>,
    @SerializedName("release_date")
    val releaseDate: String,
    val revenue: Int,
    val runtime: Int,
    @SerializedName("spoken_languages")
    val spokenLanguages: List<Any>,
    val status: String,
    val tagline: String,
    val title: String,
    val video: Boolean,
    @SerializedName("vote_average")
    val voteAverage: Int,
    @SerializedName("vote_count")
    val voteCount: Int
): UI