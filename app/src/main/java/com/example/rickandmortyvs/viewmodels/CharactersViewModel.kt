package com.example.rickandmortyvs.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.rickandmortyvs.domain.models.Characters
import com.example.rickandmortyvs.domain.models.CharactersSearchOptions
import com.example.rickandmortyvs.domain.models.Gender
import com.example.rickandmortyvs.domain.models.SearchParameters
import com.example.rickandmortyvs.domain.models.Status
import com.example.rickandmortyvs.domain.usecases.GetCharactersListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val getCharactersListUseCase: GetCharactersListUseCase
) : ViewModel() {

    private val _nameStateFlow = MutableStateFlow<String?>("")
    val nameStateFlow: StateFlow<String?> = _nameStateFlow

    private val _statusStateFlow = MutableStateFlow(Status.EMPTY)
    val statusStateFlow: StateFlow<Status> = _statusStateFlow

    private val _speciesStateFlow = MutableStateFlow<String?>("")
    val speciesStateFlow: StateFlow<String?> = _speciesStateFlow

    private val _typeStateFlow = MutableStateFlow<String?>("")
    val typeStateFlow: StateFlow<String?> = _typeStateFlow

    private val _genderStateFlow = MutableStateFlow(Gender.EMPTY)
    val genderStateFlow: StateFlow<Gender> = _genderStateFlow


    fun getCharactersList(): Flow<PagingData<Characters>> {
        return combine(
            nameStateFlow,
            statusStateFlow,
            speciesStateFlow,
            typeStateFlow,
            genderStateFlow
        ) { name, status, species, type, gender ->
            SearchParameters(
                name = name,
                status = status,
                species = species,
                type = type,
                gender = gender
            )
        }.flatMapLatest { searchParameters ->
            getCharactersListUseCase.invoke(
                searchParameters.name,
                searchParameters.status.statusType,
                searchParameters.species,
                searchParameters.type,
                searchParameters.gender.ganderType
            )
        }.cachedIn(viewModelScope)
    }

    fun addGenderFilter(gender: Gender) {
        _genderStateFlow.value = gender
    }

    fun addStatusFilter(status: Status) {
        _statusStateFlow.value = status
    }

    fun removeFilters() {
        _genderStateFlow.value = Gender.EMPTY
        _statusStateFlow.value = Status.EMPTY
    }

    fun clearSearch() {
        _nameStateFlow.value = null
        _speciesStateFlow.value = null
        _typeStateFlow.value = null
    }

    fun setSearchParameters(charactersSearchOptions: CharactersSearchOptions, searchText: String) {
        when (charactersSearchOptions) {
            CharactersSearchOptions.NAME -> _nameStateFlow.value = searchText
            CharactersSearchOptions.SPECIES -> _speciesStateFlow.value = searchText
            CharactersSearchOptions.TYPE -> _typeStateFlow.value = searchText
        }
    }


}