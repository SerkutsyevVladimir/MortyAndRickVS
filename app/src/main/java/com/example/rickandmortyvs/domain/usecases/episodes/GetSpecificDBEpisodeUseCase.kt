package com.example.rickandmortyvs.domain.usecases.episodes

import com.example.rickandmortyvs.domain.models.episodes.Episode
import com.example.rickandmortyvs.domain.repository.episode.EpisodesRepository
import javax.inject.Inject

class GetSpecificDBEpisodeUseCase @Inject constructor(
    private val episodeRepositoryImpl: EpisodesRepository
) {

    suspend operator fun invoke(id: Int): Result<Episode?> {
        return runCatching { episodeRepositoryImpl.getSpecificDBEpisode(id) }
    }
}