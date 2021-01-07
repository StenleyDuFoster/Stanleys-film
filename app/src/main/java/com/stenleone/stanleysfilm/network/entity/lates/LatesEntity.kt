package com.stenleone.stanleysfilm.network.entity.lates

import com.google.gson.annotations.SerializedName
import com.stenleone.stanleysfilm.network.entity.movie.Genre
import com.stenleone.stanleysfilm.network.entity.movie.ProductionCompany
import com.stenleone.stanleysfilm.network.entity.movie.ProductionCountry

data class LatesEntity(
    val adult: Boolean,
    @SerializedName("backdrop_path")
    val backdropPath: Any,
    @SerializedName("belongs_to_collection")
    val belongsToCollection: Any,
    val budget: Int,
    val genres: List<Genre>,
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
    val productionCompanies: List<ProductionCompany>,
    @SerializedName("production_countries")
    val productionCountries: List<ProductionCountry>,
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
)