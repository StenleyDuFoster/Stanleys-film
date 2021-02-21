package com.stenleone.stanleysfilm.network.entity.movie

import com.google.gson.annotations.SerializedName
import com.stenleone.stanleysfilm.interfaces.model.Network

data class MovieDetailsEntity(
    val adult: String?,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @SerializedName("belongs_to_collection")
    val belongsToCollection: BelongsToCollection?,
    val budget: String?,
    val genres: ArrayList<Genre>?,
    val homepage: String?,
    val id: String?,
    @SerializedName("imdb_id")
    val imdbId: String?,
    @SerializedName("original_language")
    val originalLanguage: String?,
    @SerializedName("original_title")
    val originalTitle: String?,
    val overview: String?,
    val popularity: String?,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("production_companies")
    val productionCompanies: ArrayList<ProductionCompany>?,
    @SerializedName("production_countries")
    val productionCountries: ArrayList<ProductionCountry>?,
    @SerializedName("release_date")
    val releaseDate: String?,
    val revenue: String?,
    val runtime: String?,
    @SerializedName("spoken_languages")
    val spokenLanguages: ArrayList<SpokenLanguage>?,
    val status: String?,
    @SerializedName("tagline")
    val tagLine: String?,
    val title: String?,
    val video: String?,
    @SerializedName("vote_average")
    val voteAverage: String?,
    @SerializedName("vote_count")
    val voteCount: String?
): Network