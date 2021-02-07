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

class VideosMapper @Inject constructor(
    val videosResultsMapper: VideosResualtMapper
) : UiNetworkMapper<Videos, VideosUI> {

    override fun mapFromEntity(entity: Videos): VideosUI {
        val results = ArrayList<VideosResultUI>()
        entity.results?.forEach {
            results.add(videosResultsMapper.mapFromEntity(it))
        }
        return VideosUI(
            entity.id?.toIntOrNull() ?: 0,
            results
        )
    }

    override fun mapToEntity(domainModel: VideosUI): Videos {
        val results = ArrayList<VideosResult>()
        domainModel.results?.forEach {
            results.add(videosResultsMapper.mapToEntity(it))
        }
        return Videos(
            domainModel.id.toString(),
            results
        )
    }

}