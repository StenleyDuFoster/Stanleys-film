package com.stenleone.stanleysfilm.network.mapper

import com.stenleone.stanleysfilm.interfaces.UiNetworkMapper
import com.stenleone.stanleysfilm.network.entity.Videos
import com.stenleone.stanleysfilm.network.entity.VideosResult
import com.stenleone.stanleysfilm.network.entity.movie.Movie
import com.stenleone.stanleysfilm.network.entity.movie.MovieUI
import com.stenleone.stanleysfilm.network.entity.movie.MoviesEntity
import com.stenleone.stanleysfilm.network.entity.movie.MoviesEntityUI
import com.stenleone.stanleysfilm.ui.model.VideosResultUI
import com.stenleone.stanleysfilm.ui.model.VideosUI
import javax.inject.Inject

class VideosResualtMapper @Inject constructor() : UiNetworkMapper<VideosResult, VideosResultUI> {

    override fun mapFromEntity(entity: VideosResult): VideosResultUI {
        return VideosResultUI(
            entity.id ?: "",
            entity.iso_3166_1 ?: "",
            entity.iso_639_1 ?: "",
            entity.key ?: "",
            entity.name ?: "",
            entity.site ?: "",
            entity.size?.toIntOrNull() ?: 0,
            entity.type ?: ""
        )
    }

    override fun mapToEntity(domainModel: VideosResultUI): VideosResult {
        return VideosResult(
            domainModel.id,
            domainModel.iso_3166_1,
            domainModel.iso_639_1,
            domainModel.key,
            domainModel.name,
            domainModel.site,
            domainModel.size.toString(),
            domainModel.type
        )
    }
}