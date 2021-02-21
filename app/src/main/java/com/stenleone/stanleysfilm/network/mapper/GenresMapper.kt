package com.stenleone.stanleysfilm.network.mapper

import com.stenleone.stanleysfilm.interfaces.UiNetworkMapper
import com.stenleone.stanleysfilm.network.entity.movie.*
import javax.inject.Inject

class GenresMapper @Inject constructor() : UiNetworkMapper<Genre, GenreUI> {

    override fun mapFromEntity(entity: Genre): GenreUI {
        with(entity) {
            return GenreUI(
                id?.toIntOrNull() ?: 0,
                name ?: ""
            )
        }
    }

    override fun mapToEntity(domainModel: GenreUI): Genre {
        with(domainModel) {
            return Genre(
                id.toString(),
                name
            )
        }
    }

}