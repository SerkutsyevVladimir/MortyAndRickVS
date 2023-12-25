package com.example.rickandmortyvs.domain.models.episodes

import androidx.paging.PagingData

data class EpisodeParameters(
    val episodes: PagingData<Episode>? = PagingData.empty()
)