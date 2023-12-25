package com.example.rickandmortyvs.domain.mappers.rest.episode

import com.example.rickandmortyvs.data.database.models.episode.DBEpisode
import com.example.rickandmortyvs.data.network.models.episodes.RestEpisodesList
import com.example.rickandmortyvs.domain.mappers.Mapper
import com.example.rickandmortyvs.domain.models.episodes.Episode

class RestEpisodeMapper : Mapper<RestEpisodesList.RestEpisode, Episode> {
    override fun map(input: RestEpisodesList.RestEpisode): Episode {
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

    fun mapToDBModel(input: RestEpisodesList.RestEpisode): DBEpisode {
        return DBEpisode(
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