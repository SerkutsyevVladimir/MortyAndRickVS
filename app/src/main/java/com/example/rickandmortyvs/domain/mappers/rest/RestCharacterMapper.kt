package com.example.rickandmortyvs.domain.mappers.rest

import com.example.rickandmortyvs.data.database.models.DBCharacter
import com.example.rickandmortyvs.data.database.models.DBLocation
import com.example.rickandmortyvs.data.database.models.DBOrigin
import com.example.rickandmortyvs.data.network.models.RestCharacter
import com.example.rickandmortyvs.domain.mappers.Mapper
import com.example.rickandmortyvs.domain.models.Characters
import com.example.rickandmortyvs.domain.models.Location
import com.example.rickandmortyvs.domain.models.Origin

class RestCharacterMapper: Mapper<RestCharacter,Characters> {
    override fun map(input: RestCharacter): Characters {
        return Characters(
            id = input.id,
            name = input.name,
            status = input.status,
            species = input.species,
            type = input.type,
            gender = input.gender,
            origin = Origin(
                name = input.origin.name,
                url = input.origin.url
            ),
            location = Location(
                name = input.location.name,
                url = input.location.url
            ),
            image = input.image,
            episode = input.episode,
            url = input.url,
            created = input.created
        )
    }

    fun mapToDBModel(input: RestCharacter): DBCharacter{
        return DBCharacter(
            id = input.id,
            name = input.name,
            status = input.status,
            species = input.species,
            type = input.type,
            gender = input.gender,
            origin = DBOrigin(
                name = input.origin.name,
                url = input.origin.url
            ),
            location = DBLocation(
                name = input.location.name,
                url = input.location.url
            ),
            image = input.image,
            episode = input.episode,
            url = input.url,
            created = input.created
        )
    }


}