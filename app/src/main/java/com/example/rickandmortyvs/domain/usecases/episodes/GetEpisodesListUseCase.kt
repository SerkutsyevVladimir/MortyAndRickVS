package com.example.rickandmortyvs.domain.usecases.episodes

import androidx.paging.PagingData
import androidx.paging.map
import com.example.rickandmortyvs.domain.mappers.db.episode.DBEpisodeMapper
import com.example.rickandmortyvs.domain.models.episodes.Episode
import com.example.rickandmortyvs.domain.repository.episode.EpisodesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetEpisodesListUseCase @Inject constructor(
    private val episodesRepositoryImpl: EpisodesRepository,
    private val dbEpisodeMapper: DBEpisodeMapper
) {

    suspend operator fun invoke(
        name: String?,
        episode: String?
    ): Flow<PagingData<Episode>> {
        return episodesRepositoryImpl
            .getEpisodesList(
                name,
                episode
            ).flow.map { it -> it.map { dbEpisodeMapper.map(it) } }
    }
}