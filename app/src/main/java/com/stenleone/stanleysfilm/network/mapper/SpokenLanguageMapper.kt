package com.stenleone.stanleysfilm.network.mapper

import com.stenleone.stanleysfilm.interfaces.UiNetworkMapper
import com.stenleone.stanleysfilm.network.entity.movie.*
import javax.inject.Inject

class SpokenLanguageMapper @Inject constructor() : UiNetworkMapper<SpokenLanguage, SpokenLanguageUI> {

    override fun mapFromEntity(entity: SpokenLanguage): SpokenLanguageUI {
        with(entity) {
            return SpokenLanguageUI(
                englishName ?: "",
                iso_639_1 ?: "",
                name ?: ""
            )
        }
    }

    override fun mapToEntity(domainModel: SpokenLanguageUI): SpokenLanguage {
        with(domainModel) {
            return SpokenLanguage(
                englishName,
                iso_639_1,
                name
            )
        }
    }

}