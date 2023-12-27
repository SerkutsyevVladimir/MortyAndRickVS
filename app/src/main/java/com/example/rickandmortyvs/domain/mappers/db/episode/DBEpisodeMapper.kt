package com.example.rickandmortyvs.domain.mappers.db.episode

import com.example.rickandmortyvs.data.database.models.episode.DBEpisode
import com.example.rickandmortyvs.domain.mappers.Mapper
import com.example.rickandmortyvs.domain.models.episodes.Episode

class DBEpisodeMapper : Mapper<DBEpisode, Episode> {
    override fun map(input: DBEpisode): Episode {
        return Episode(
            id = input.id,
            name = input.name,
            airDate = input.airDate,
            episode = input.episode,
            characters = input.characters,
            url = input.url,
            created = input.created
        )
    }
}