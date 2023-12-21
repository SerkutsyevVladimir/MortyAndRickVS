package com.example.rickandmortyvs.domain.mappers.db.location

import com.example.rickandmortyvs.data.database.models.location.DBLocationDetails
import com.example.rickandmortyvs.domain.mappers.Mapper
import com.example.rickandmortyvs.domain.models.locations.LocationDetails

class DBLocationDetailsMapper: Mapper<DBLocationDetails, LocationDetails> {
    override fun map(input: DBLocationDetails): LocationDetails {
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
}