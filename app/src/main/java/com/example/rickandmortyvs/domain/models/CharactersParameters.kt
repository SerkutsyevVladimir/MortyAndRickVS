package com.example.rickandmortyvs.domain.models

import androidx.paging.PagingData

data class CharactersParameters(
    val characters: PagingData<Characters>? = PagingData.empty(),
    val isFilteringEnabled: Boolean = false
)