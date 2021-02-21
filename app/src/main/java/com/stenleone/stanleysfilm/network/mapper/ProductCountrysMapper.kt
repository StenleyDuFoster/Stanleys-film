package com.stenleone.stanleysfilm.network.mapper

import com.stenleone.stanleysfilm.interfaces.UiNetworkMapper
import com.stenleone.stanleysfilm.network.entity.movie.*
import javax.inject.Inject

class ProductCountrysMapper @Inject constructor() : UiNetworkMapper<ProductionCountry, ProductionCountryUI> {

    override fun mapFromEntity(entity: ProductionCountry): ProductionCountryUI {
        with(entity) {
            return ProductionCountryUI(
                iso_3166_1 ?: "",
                name ?: ""
            )
        }
    }

    override fun mapToEntity(domainModel: ProductionCountryUI): ProductionCountry {
        with(domainModel) {
            return ProductionCountry(
                iso_3166_1,
                name
            )
        }
    }

}