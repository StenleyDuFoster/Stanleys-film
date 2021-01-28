package com.stenleone.stanleysfilm.network.entity.movie

import com.google.gson.annotations.SerializedName
import com.stenleone.stanleysfilm.interfaces.model.UI

data class MovieDetailsEntityUI(
    val adult: Boolean,
    @SerializedName("backdrop_path")
    val backdropPath: String,
    @SerializedName("belongs_to_collection")
    val belongsToCollectionUI: BelongsToCollectionUI,
    val budget: Int?,
    val genres: ArrayList<GenreUI>,
    val homepage: String,
    val id: Int,
    @SerializedName("imdb_id")
    val imdbId: String?,
    @SerializedName("original_language")
    val originalLanguage: String,
    @SerializedName("original_title")
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("production_companies")
    val productionCompaniesUI: ArrayList<ProductionCompanyUI>,
    @SerializedName("production_countries")
    val productionCountriesUI: ArrayList<ProductionCountryUI>,
    @SerializedName("release_date")
    val releaseDate: String,
    val revenue: Int,
    val runtime: Int,
    @SerializedName("spoken_languages")
    val spokenLanguagesUI: ArrayList<SpokenLanguageUI>,
    val status: String,
    @SerializedName("tagline")
    val tagLine: String,
    val title: String,
    val video: Boolean,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("vote_count")
    val voteCount: Int
): UI