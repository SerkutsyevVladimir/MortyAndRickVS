package com.example.rickandmortyvs.domain.usecases.episodes

import com.example.rickandmortyvs.domain.models.episodes.Episode
import com.example.rickandmortyvs.domain.repository.episode.EpisodesRepository
import javax.inject.Inject

class GetMultipleDBEpisodesUseCase @Inject constructor(
    private val episodesRepositoryImpl: EpisodesRepository
) {

    suspend operator fun invoke(ids: List<Int>): Result<List<Episode>?>{
        return runCatching {episodesRepositoryImpl.getMultipleDBEpisodes(ids)}
    }
}