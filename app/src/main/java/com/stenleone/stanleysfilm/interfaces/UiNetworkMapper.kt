package com.stenleone.stanleysfilm.interfaces

interface UiNetworkMapper<Network, UI> {

    fun mapFromEntity(entity: Network): UI

    fun mapToEntity(domainModel: UI): Network

    fun mapFromEntityList(entities: List<Network>): List<UI> {
        return entities.map { e -> mapFromEntity(e) }
    }
}