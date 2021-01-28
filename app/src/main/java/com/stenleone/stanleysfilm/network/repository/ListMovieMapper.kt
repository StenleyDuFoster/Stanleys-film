package com.stenleone.stanleysfilm.network.repository

import com.stenleone.stanleysfilm.interfaces.UiNetworkMapper
import com.stenleone.stanleysfilm.network.entity.movie.MoviesEntity
import com.stenleone.stanleysfilm.network.entity.movie.MoviesEntityUI
import javax.inject.Inject

class ListMovieMapper @Inject constructor(): UiNetworkMapper<MoviesEntity, MoviesEntityUI> {

    override fun mapFromEntity(entity: MoviesEntity): MoviesEntityUI {
        return MoviesEntityUI(
            entity.page?.toInt() ?: 0,
            entity.movies ?: arrayListOf(),
            entity.totalPages?.toInt() ?: 0,
            entity.totalResults?.toInt() ?: 0
        )
    }

    override fun mapToEntity(domainModel: MoviesEntityUI): MoviesEntity {
        return MoviesEntity(
            domainModel.page.toString(),
            domainModel.movies,
            domainModel.totalPages.toString(),
            domainModel.totalResults.toString()
        )
    }

}