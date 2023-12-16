package com.example.rickandmortyvs.domain.mappers.db

import com.example.rickandmortyvs.data.database.models.DBCharacter
import com.example.rickandmortyvs.domain.mappers.Mapper
import com.example.rickandmortyvs.domain.models.Characters
import com.example.rickandmortyvs.domain.models.Location
import com.example.rickandmortyvs.domain.models.Origin

class DBCharacterMapper: Mapper<DBCharacter, Characters> {
    override fun map(input: DBCharacter): Characters {
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

}