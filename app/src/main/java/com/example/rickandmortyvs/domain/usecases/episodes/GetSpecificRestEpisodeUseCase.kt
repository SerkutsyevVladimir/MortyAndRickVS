package com.example.rickandmortyvs.domain.usecases.episodes

import com.example.rickandmortyvs.domain.models.episodes.Episode
import com.example.rickandmortyvs.domain.repository.episode.EpisodesRepository
import javax.inject.Inject

class GetSpecificRestEpisodeUseCase @Inject constructor(
    private val episodesRepositoryImpl: EpisodesRepository
) {
    suspend operator fun invoke(id: Int): Result<Episode?> {
        return runCatching { episodesRepositoryImpl.getSpecificRestEpisode(id) }
    }
}