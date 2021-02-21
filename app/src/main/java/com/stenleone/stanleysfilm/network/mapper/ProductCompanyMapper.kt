package com.stenleone.stanleysfilm.network.mapper

import com.stenleone.stanleysfilm.interfaces.UiNetworkMapper
import com.stenleone.stanleysfilm.network.entity.movie.*
import javax.inject.Inject

class ProductCompanyMapper @Inject constructor() : UiNetworkMapper<ProductionCompany, ProductionCompanyUI> {

    override fun mapFromEntity(entity: ProductionCompany): ProductionCompanyUI {
        with(entity) {
            return ProductionCompanyUI(
                id?.toIntOrNull() ?: 0,
                logoPath ?: "",
                name ?: "",
                originCountry ?: ""
            )
        }
    }

    override fun mapToEntity(domainModel: ProductionCompanyUI): ProductionCompany {
        with(domainModel) {
            return ProductionCompany(
                id.toString(),
                logoPath,
                name,
                originCountry
            )
        }
    }

}