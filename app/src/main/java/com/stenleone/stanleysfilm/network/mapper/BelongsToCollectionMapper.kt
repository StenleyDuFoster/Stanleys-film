package com.stenleone.stanleysfilm.network.mapper

import com.stenleone.stanleysfilm.interfaces.UiNetworkMapper
import com.stenleone.stanleysfilm.network.entity.movie.*
import javax.inject.Inject

class BelongsToCollectionMapper @Inject constructor() : UiNetworkMapper<BelongsToCollection, BelongsToCollectionUI> {

    override fun mapFromEntity(entity: BelongsToCollection): BelongsToCollectionUI {
        with(entity) {
            return BelongsToCollectionUI(
                backdropPath ?: "",
                id?.toIntOrNull() ?: 0,
                name ?: "",
                posterPath ?: ""
            )
        }
    }

    override fun mapToEntity(domainModel: BelongsToCollectionUI): BelongsToCollection {
        with(domainModel) {
            return BelongsToCollection(
                backdropPath,
                id.toString(),
                name,
                posterPath
            )
        }
    }

}