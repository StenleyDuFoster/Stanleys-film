package com.stenleone.stanleysfilm.network.mapper

import com.stenleone.stanleysfilm.interfaces.UiNetworkMapper
import com.stenleone.stanleysfilm.network.entity.movie.Movie
import com.stenleone.stanleysfilm.network.entity.movie.MovieUI
import com.stenleone.stanleysfilm.network.entity.movie.MoviesEntity
import com.stenleone.stanleysfilm.network.entity.movie.MoviesEntityUI
import javax.inject.Inject

class MovieMapper @Inject constructor() : UiNetworkMapper<Movie, MovieUI> {

    override fun mapFromEntity(entity: Movie): MovieUI {
        val genres = arrayListOf<Int>()
        entity.genreIds?.forEach {
            it.toIntOrNull()?.let { genreId ->
                genres.add(genreId)
            }
        }
        return MovieUI(
            entity.adult?.toBoolean() ?: false,
            entity.backdropPath ?: "",
            genres,
            entity.id?.toIntOrNull() ?: 0,
            entity.originalLanguage ?: "",
            entity.originalTitle ?: "",
            entity.overview ?: "",
            entity.popularity?.toFloatOrNull() ?: 0.0f,
            entity.posterPath ?: "",
            entity.releaseDate ?: "",
            entity.title ?: "",
            entity.video?.toBoolean() ?: false,
            entity.voteAverage?.toFloatOrNull() ?: 0.0f,
            entity.voteCount?.toIntOrNull() ?: 0
        )
    }

    override fun mapToEntity(domainModel: MovieUI): Movie {
        val genres = arrayListOf<String>()
        domainModel.genreIds.forEach {
            genres.add(it.toString())
        }
        return Movie(
            domainModel.adult.toString(),
            domainModel.backdropPath,
            genres,
            domainModel.id.toString(),
            domainModel.originalLanguage,
            domainModel.originalTitle,
            domainModel.overview,
            domainModel.popularity.toString(),
            domainModel.posterPath,
            domainModel.releaseDate,
            domainModel.title,
            domainModel.video.toString(),
            domainModel.voteAverage.toString(),
            domainModel.voteCount.toString()
        )
    }

}