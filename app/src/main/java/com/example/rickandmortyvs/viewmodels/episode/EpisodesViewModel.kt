package com.example.rickandmortyvs.viewmodels.episode

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.rickandmortyvs.domain.models.episodes.Episode
import com.example.rickandmortyvs.domain.models.episodes.EpisodeSearchOptions
import com.example.rickandmortyvs.domain.models.episodes.EpisodeSearchParameters
import com.example.rickandmortyvs.domain.usecases.episodes.GetEpisodesListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class EpisodesViewModel @Inject constructor(
    private val getEpisodesListUseCase: GetEpisodesListUseCase
) : ViewModel() {

    private val _nameStateFlow = MutableStateFlow<String?>("")
    val nameStateFlow: StateFlow<String?> = _nameStateFlow

    private val _episodeStateFlow = MutableStateFlow<String?>("")
    val episodeStateFlow: StateFlow<String?> = _episodeStateFlow


    fun getEpisodesList(): Flow<PagingData<Episode>> {
        return combine(
            nameStateFlow,
            episodeStateFlow
        ) { name, episode ->
            EpisodeSearchParameters(
                name = name,
                episode = episode
            )
        }.flatMapLatest { searchParameters ->
            getEpisodesListUseCase.invoke(
                searchParameters.name,
                searchParameters.episode
            )
        }.cachedIn(viewModelScope)
    }

    fun clearSearch() {
        _nameStateFlow.value = null
        _episodeStateFlow.value = null
    }

    fun setSearchParameters(episodeSearchOptions: EpisodeSearchOptions, searchText: String) {
        when (episodeSearchOptions) {
            EpisodeSearchOptions.NAME -> _nameStateFlow.value = searchText
            EpisodeSearchOptions.EPISODE -> _episodeStateFlow.value = searchText
        }
    }


}