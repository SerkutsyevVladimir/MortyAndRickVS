package com.example.rickandmortyvs.domain.repository.episode

import androidx.paging.Pager
import com.example.rickandmortyvs.data.database.models.episode.DBEpisode
import com.example.rickandmortyvs.domain.models.episodes.Episode

interface EpisodesRepository {
    suspend fun getEpisodesList(
        name: String?,
        episode: String?
    ): Pager<Int, DBEpisode>

    suspend fun getSpecificRestEpisode(id: Int): Episode?

    suspend fun getMultipleRestEpisodes(ids: List<Int>): List<Episode>?

    suspend fun getSpecificDBEpisode(id: Int): Episode?

    suspend fun getMultipleDBEpisodes(ids: List<Int>): List<Episode>?
}