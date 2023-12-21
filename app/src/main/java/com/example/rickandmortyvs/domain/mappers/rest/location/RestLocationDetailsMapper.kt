package com.example.rickandmortyvs.domain.mappers.rest.location

import com.example.rickandmortyvs.data.database.models.location.DBLocationDetails
import com.example.rickandmortyvs.data.network.models.locations.RestLocationsList
import com.example.rickandmortyvs.domain.mappers.Mapper
import com.example.rickandmortyvs.domain.models.locations.LocationDetails

class RestLocationDetailsMapper : Mapper<RestLocationsList.RestLocationDetails, LocationDetails> {
    override fun map(input: RestLocationsList.RestLocationDetails): LocationDetails {
        return LocationDetails(
            id = input.id,
            name = input.name,
            type = input.type,
            dimension = input.dimension,
            residents = input.residents,
            url = input.url,
            created = input.created
        )
    }

    fun mapToDBModel(input: RestLocationsList.RestLocationDetails): DBLocationDetails {
        return DBLocationDetails(
            id = input.id,
            name = input.name,
            type = input.type,
            dimension = input.dimension,
            residents = input.residents,
            url = input.url,
            created = input.created
        )
    }

}