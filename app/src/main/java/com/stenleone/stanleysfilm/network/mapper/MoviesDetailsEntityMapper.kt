package com.stenleone.stanleysfilm.network.mapper

import com.stenleone.stanleysfilm.interfaces.UiNetworkMapper
import com.stenleone.stanleysfilm.network.entity.movie.*
import javax.inject.Inject

class MoviesDetailsEntityMapper @Inject constructor(
    val belongsToCollectionMapper: BelongsToCollectionMapper,
    val genresMapper: GenresMapper,
    val productCompanyMapper: ProductCompanyMapper,
    val productCountrysMapper: ProductCountrysMapper,
    val spokenLanguageMapper: SpokenLanguageMapper
) : UiNetworkMapper<MovieDetailsEntity, MovieDetailsEntityUI> {

    override fun mapFromEntity(entity: MovieDetailsEntity): MovieDetailsEntityUI {
        with(entity) {
            return MovieDetailsEntityUI(
                adult?.toBoolean() ?: false,
                backdropPath ?: "",
                belongsToCollection?.let { belongsToCollectionMapper.mapFromEntity(it) },
                budget?.toIntOrNull() ?: 0,
                ArrayList(genres?.let { genresMapper.mapFromEntityList(it.toList()) }),
                homepage ?: "",
                id?.toIntOrNull() ?: 0,
                imdbId ?: "",
                originalLanguage ?: "",
                originalTitle ?: "",
                overview ?: "",
                popularity?.toDoubleOrNull() ?: 0.0,
                posterPath ?: "",
                ArrayList(productionCompanies?.toList()?.let { productCompanyMapper.mapFromEntityList(it) }),
                ArrayList(productionCountries?.toList()?.let { productCountrysMapper.mapFromEntityList(it) }),
                releaseDate ?: "",
                revenue?.toIntOrNull() ?: 0,
                runtime?.toIntOrNull() ?: 0,
                ArrayList(spokenLanguages?.toList()?.let { spokenLanguageMapper.mapFromEntityList(it) }),
                status ?: "",
                tagLine ?: "",
                title ?: "",
                video?.toBoolean() ?: false,
                voteAverage?.toDoubleOrNull() ?: 0.0,
                voteCount?.toIntOrNull() ?: 0
            )
        }
    }

    override fun mapToEntity(domainModel: MovieDetailsEntityUI): MovieDetailsEntity {
        with(domainModel) {
            return MovieDetailsEntity(
                adult.toString(),
                backdropPath,
                belongsToCollectionUI?.let { belongsToCollectionMapper.mapToEntity(it) },
                budget.toString(),
                ArrayList(genres?.let { genresMapper.mapToEntityList(it.toList()) }),
                homepage,
                id.toString(),
                imdbId,
                originalLanguage,
                originalTitle,
                overview,
                popularity.toString(),
                posterPath,
                ArrayList(productionCompaniesUI?.toList()?.let { productCompanyMapper.mapToEntityList(it) }),
                ArrayList(productionCountriesUI?.toList()?.let { productCountrysMapper.mapToEntityList(it) }),
                releaseDate,
                revenue.toString(),
                runtime.toString(),
                ArrayList(spokenLanguagesUI?.toList()?.let { spokenLanguageMapper.mapToEntityList(it) }),
                status,
                tagLine,
                title,
                video.toString(),
                voteAverage.toString(),
                voteCount.toString()
            )
        }
    }

}