package com.stenleone.stanleysfilm.network.mapper

import com.stenleone.stanleysfilm.interfaces.UiNetworkMapper
import com.stenleone.stanleysfilm.network.entity.movie.Movie
import com.stenleone.stanleysfilm.network.entity.movie.MovieUI
import com.stenleone.stanleysfilm.network.entity.movie.MoviesEntity
import com.stenleone.stanleysfilm.network.entity.movie.MoviesEntityUI
import javax.inject.Inject

class MoviesEntityMapper @Inject constructor(
    val movieMapper: MovieMapper
): UiNetworkMapper<MoviesEntity, MoviesEntityUI> {

    override fun mapFromEntity(entity: MoviesEntity): MoviesEntityUI {
        val movies = ArrayList<MovieUI>()
        entity.movies?.forEach {
            movies.add(movieMapper.mapFromEntity(it))
        }
        return MoviesEntityUI(
            entity.page?.toInt() ?: 0,
            movies,
            entity.totalPages?.toInt() ?: 0,
            entity.totalResults?.toInt() ?: 0
        )
    }

    override fun mapToEntity(domainModel: MoviesEntityUI): MoviesEntity {
        val movies = ArrayList<Movie>()
        domainModel.movies?.forEach {
            movies.add(movieMapper.mapToEntity(it))
        }
        return MoviesEntity(
            domainModel.page.toString(),
            movies,
            domainModel.totalPages.toString(),
            domainModel.totalResults.toString()
        )
    }

}